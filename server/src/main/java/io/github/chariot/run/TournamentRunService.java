package io.github.chariot.run;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.github.chariot.algo.DefaultRoundRobinAlgorithm;
import io.github.chariot.algo.FinishRecord;
import io.github.chariot.algo.LaneAssignment;
import io.github.chariot.algo.LegResult;
import io.github.chariot.algo.TournamentAlgorithm;
import io.github.chariot.model.Racer;
import io.github.chariot.model.TournamentConfig;
import io.github.chariot.model.TrackConfig;
import io.github.chariot.service.ConfigService;

@Service
public class TournamentRunService {
    private final ConfigService configService;

    private final Map<String, TournamentAlgorithm> algorithms = new HashMap<>();
    private final Map<String, TournamentRun> runs = new ConcurrentHashMap<>();

    public TournamentRunService(ConfigService configService) {
        this.configService = configService;
        // register algorithms (future: DI / plugin)
        algorithms.put("default-round-robin", new DefaultRoundRobinAlgorithm());
    }

    public List<Map<String, String>> listAlgorithms() {
        return algorithms.values().stream()
                .map(a -> Map.of("id", a.id(), "name", a.displayName()))
                .collect(Collectors.toList());
    }

    public TournamentRun startRun(String tournamentId) {
        TournamentConfig tc = configService.getTournament(tournamentId)
                .orElseThrow(() -> new NoSuchElementException("Tournament not found: " + tournamentId));
        TrackConfig track = configService.getTrack(tc.getTrackConfigId())
                .orElseThrow(() -> new NoSuchElementException("Track not found: " + tc.getTrackConfigId()));
        List<Racer> racers = tc.getRacerIds().stream()
                .map(id -> configService.getRacer(id).orElseThrow(() -> new NoSuchElementException("Racer not found: " + id)))
                .collect(Collectors.toList());

        TournamentAlgorithm algo = algorithms.get(tc.getAlgorithmId());
        if (algo == null) throw new IllegalArgumentException("Unknown algorithm: " + tc.getAlgorithmId());

        // configure algorithm for this run (clone-ish by creating a new instance)
        TournamentAlgorithm runAlgo = new DefaultRoundRobinAlgorithm();
        runAlgo.setConfig(tc.getAlgorithmConfig());
        runAlgo.setRacers(racers);
        runAlgo.setTrack(track);

        TournamentRun run = new TournamentRun(tournamentId);
        // stash algorithm instance under runId by wrapping in a map
        runs.put(run.getRunId(), run);
        // store algorithm in state map via synthetic key
        runContext.put(run.getRunId(), new RunContext(tc, track, racers, runAlgo));
        return run;
    }

    private static class RunContext {
        TournamentConfig tournament;
        TrackConfig track;
        List<Racer> racers;
        TournamentAlgorithm algo;
        RunContext(TournamentConfig t, TrackConfig track, List<Racer> racers, TournamentAlgorithm algo) {
            this.tournament = t; this.track = track; this.racers = racers; this.algo = algo;
        }
    }
    private final Map<String, RunContext> runContext = new ConcurrentHashMap<>();

    public RunView view(String runId) {
        TournamentRun run = getRun(runId);
        RunContext ctx = getCtx(runId);

        RunView v = new RunView();
        v.setRunId(run.getRunId());
        v.setState(run.getState());
        v.setCurrentLeg(run.getCurrentLeg());
        v.setLastLegResult(run.getLastLegResult());

        if (run.getCurrentLeg() != null) {
            Map<String, Racer> byId = ctx.racers.stream().collect(Collectors.toMap(Racer::getId, r -> r));
            List<RunView.RacerSummary> summaries = new ArrayList<>();
            for (LaneAssignment la : run.getCurrentLeg().getAssignments()) {
                Racer r = byId.get(la.getRacerId());
                if (r != null) summaries.add(new RunView.RacerSummary(r, la.getLaneNumber(), la.getLaneColor()));
            }
            v.setCurrentLegRacers(summaries);
        }

        if (run.getState() == RunState.TOURNAMENT_COMPLETE) {
            v.setRankings(ctx.algo.rankings());
        }

        return v;
    }

    public TournamentRun getRun(String runId) {
        TournamentRun run = runs.get(runId);
        if (run == null) throw new NoSuchElementException("Run not found: " + runId);
        return run;
    }

    private RunContext getCtx(String runId) {
        RunContext ctx = runContext.get(runId);
        if (ctx == null) throw new NoSuchElementException("Run context not found: " + runId);
        return ctx;
    }

    public RunView nextLeg(String runId) {
        TournamentRun run = getRun(runId);
        RunContext ctx = getCtx(runId);

        if (ctx.algo.isComplete()) {
            run.setState(RunState.TOURNAMENT_COMPLETE);
            return view(runId);
        }

        run.setCurrentLeg(ctx.algo.nextLeg());
        run.setLaneFinishTimes(new HashMap<>());
        run.setStartSignalTime(null);
        run.setLastLegResult(null);

        run.setState(RunState.AWAITING_CLIENT_ACK_START);
        return view(runId);
    }

    public RunView clientAckStart(String runId) {
        TournamentRun run = getRun(runId);
        if (run.getState() != RunState.AWAITING_CLIENT_ACK_START) {
            throw new IllegalStateException("Not awaiting client ack");
        }
        run.setState(RunState.AWAITING_START_SIGNAL);
        return view(runId);
    }

    public RunView signalStart(String runId) {
        TournamentRun run = getRun(runId);
        RunContext ctx = getCtx(runId);

        if (run.getState() != RunState.AWAITING_START_SIGNAL) {
            throw new IllegalStateException("Not awaiting start signal");
        }
        run.setStartSignalTime(Instant.now());
        run.setState(RunState.RUNNING_LEG);

        // no background threads; completion is checked when finish signals arrive or client polls proceed
        maybeCompleteLeg(run, ctx);
        return view(runId);
    }

    public RunView signalFinish(String runId, int laneNumber) {
        TournamentRun run = getRun(runId);
        RunContext ctx = getCtx(runId);

        if (run.getState() != RunState.RUNNING_LEG) {
            throw new IllegalStateException("Leg is not running");
        }
        if (run.getStartSignalTime() == null) {
            throw new IllegalStateException("Start time missing");
        }

        int hundredths = (int)Math.round(Duration.between(run.getStartSignalTime(), Instant.now()).toMillis() / 10.0);
        run.getLaneFinishTimes().putIfAbsent(laneNumber, hundredths);

        maybeCompleteLeg(run, ctx);
        return view(runId);
    }

    private void maybeCompleteLeg(TournamentRun run, RunContext ctx) {
        if (run.getCurrentLeg() == null) return;
        int timeoutHundredths = ctx.tournament.getTimeoutSeconds() * 100;

        // Determine assigned lanes
        Set<Integer> assignedLanes = run.getCurrentLeg().getAssignments().stream()
                .map(LaneAssignment::getLaneNumber)
                .collect(Collectors.toSet());

        // If start exists, apply timeout to missing finishes when timeout exceeded
        if (run.getStartSignalTime() != null) {
            int elapsedHundredths = (int)Math.round(Duration.between(run.getStartSignalTime(), Instant.now()).toMillis() / 10.0);
            if (elapsedHundredths >= timeoutHundredths) {
                for (int lane : assignedLanes) {
                    run.getLaneFinishTimes().putIfAbsent(lane, timeoutHundredths);
                }
            }
        }

        // If all assigned lanes have finish times, build result
        boolean allFinished = assignedLanes.stream().allMatch(lane -> run.getLaneFinishTimes().containsKey(lane));
        if (!allFinished) return;

        List<FinishRecord> finishes = new ArrayList<>();
        // collect racer finish times
        for (LaneAssignment la : run.getCurrentLeg().getAssignments()) {
            int time = run.getLaneFinishTimes().getOrDefault(la.getLaneNumber(), timeoutHundredths);
            finishes.add(new FinishRecord(la.getRacerId(), time, 0));
        }
        // order by time
        finishes.sort(Comparator.comparingInt(FinishRecord::getTimeHundredths));
        for (int i=0; i<finishes.size(); i++) finishes.get(i).setPlace(i+1);

        LegResult lr = new LegResult(run.getCurrentLeg().getLegNumber(), finishes);
        run.setLastLegResult(lr);
        run.setState(RunState.LEG_COMPLETE_AWAITING_CLIENT_DECISION);
    }

    public RunView proceed(String runId) {
        TournamentRun run = getRun(runId);
        RunContext ctx = getCtx(runId);

        if (run.getState() != RunState.LEG_COMPLETE_AWAITING_CLIENT_DECISION) {
            throw new IllegalStateException("Leg not complete");
        }
        ctx.algo.recordLegResult((LegResult) run.getLastLegResult());

        if (ctx.algo.isComplete()) {
            run.setState(RunState.TOURNAMENT_COMPLETE);
        } else {
            run.setState(RunState.READY_FOR_NEXT_LEG);
        }
        return view(runId);
    }

    public RunView rerun(String runId) {
        TournamentRun run = getRun(runId);
        if (run.getCurrentLeg() == null) throw new IllegalStateException("No current leg");
        // simply reset signals and go back to ack state
        run.setLaneFinishTimes(new HashMap<>());
        run.setStartSignalTime(null);
        run.setLastLegResult(null);
        run.setState(RunState.AWAITING_CLIENT_ACK_START);
        return view(runId);
    }
}
