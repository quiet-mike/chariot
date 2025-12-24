package io.github.chariot.algo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import io.github.chariot.model.Racer;
import io.github.chariot.model.TrackConfig;

/**
 * Default algorithm:
 * - Configuration: {"rounds": <int>} how many full rounds (each racer appears in ~rounds legs).
 * - Generates legs by shuffling racers, then chunking into track lane sized legs, repeating for each round.
 * - Ranking: total time across all legs (lower wins).
 */
public class DefaultRoundRobinAlgorithm implements TournamentAlgorithm {
    private Map<String, Object> config = new HashMap<>();
    private List<Racer> racers = new ArrayList<>();
    private TrackConfig track;

    private final Deque<RaceLeg> pending = new ArrayDeque<>();
    private final Map<String, Integer> totals = new HashMap<>();
    private int totalLegs = 0;

    @Override public String id() { return "default-round-robin"; }
    @Override public String displayName() { return "Default (Total Time Round Robin)"; }

    @Override public void setConfig(Map<String, Object> config) {
        this.config = (config == null) ? new HashMap<>() : new HashMap<>(config);
    }

    @Override public void setRacers(List<Racer> racers) {
        this.racers = (racers == null) ? new ArrayList<>() : new ArrayList<>(racers);
    }

    @Override public void setTrack(TrackConfig trackConfig) {
        this.track = trackConfig;
        rebuildLegs();
    }

    private int rounds() {
        Object r = config.getOrDefault("rounds", 2);
        if (r instanceof Number n) return Math.max(1, n.intValue());
        try { return Math.max(1, Integer.parseInt(String.valueOf(r))); } catch (Exception e) { return 2; }
    }

    private void rebuildLegs() {
        pending.clear();
        totals.clear();

        if (track == null || racers == null || racers.isEmpty()) {
            totalLegs = 0;
            return;
        }

        int laneCount = Math.max(1, track.getLaneCount());
        int rounds = rounds();

        List<Racer> base = new ArrayList<>(racers);
        Random rng = new Random(119); // deterministic for repeatability

        int legNo = 1;
        for (int round=1; round<=rounds; round++) {
            Collections.shuffle(base, rng);
            for (int i=0; i<base.size(); i+=laneCount) {
                List<Racer> chunk = base.subList(i, Math.min(base.size(), i+laneCount));
                List<LaneAssignment> assigns = new ArrayList<>();
                for (int j=0; j<chunk.size(); j++) {
                    int laneNumber = track.getLanes().get(j).getLaneNumber();
                    String color = track.getLanes().get(j).getColor();
                    assigns.add(new LaneAssignment(laneNumber, color, chunk.get(j).getId()));
                }
                pending.addLast(new RaceLeg(legNo++, assigns));
            }
        }
        totalLegs = pending.size();
    }

    @Override public int totalLegCount() { return totalLegs; }
    @Override public int legsLeft() { return pending.size(); }

    @Override public RaceLeg nextLeg() {
        if (pending.isEmpty()) throw new IllegalStateException("No legs left");
        return pending.peekFirst();
    }

    @Override public void recordLegResult(LegResult result) {
        if (result == null) return;
        RaceLeg current = pending.peekFirst();
        if (current == null || current.getLegNumber() != result.getLegNumber()) {
            throw new IllegalArgumentException("Result leg number does not match current leg");
        }

        for (FinishRecord fr : result.getFinishes()) {
            totals.merge(fr.getRacerId(), fr.getTimeHundredths(), Integer::sum);
        }
        pending.removeFirst();
    }

    @Override public boolean isComplete() { return pending.isEmpty(); }

    @Override public List<RacerRanking> rankings() {
        // racers who never ran have 0 and will rank first; bump them to bottom by treating as MAX
        Map<String, Integer> score = new HashMap<>();
        for (Racer r : racers) {
            score.put(r.getId(), totals.getOrDefault(r.getId(), Integer.MAX_VALUE / 4));
        }
        List<Map.Entry<String, Integer>> sorted = score.entrySet().stream()
                .sorted(Comparator.comparingInt(Map.Entry::getValue))
                .collect(Collectors.toList());
        List<RacerRanking> out = new ArrayList<>();
        int rank = 1;
        for (var e : sorted) {
            out.add(new RacerRanking(e.getKey(), rank++, e.getValue()));
        }
        return out;
    }
}
