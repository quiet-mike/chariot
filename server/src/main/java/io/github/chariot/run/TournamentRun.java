package io.github.chariot.run;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.github.chariot.algo.LegResult;
import io.github.chariot.algo.RaceLeg;

public class TournamentRun {
    private String runId = UUID.randomUUID().toString();
    private String tournamentId;

    private RunState state = RunState.READY_FOR_NEXT_LEG;

    private RaceLeg currentLeg;
    private Instant startSignalTime;

    // laneNumber -> timeHundredths
    private Map<Integer, Integer> laneFinishTimes = new HashMap<>();

    private LegResult lastLegResult;

    public TournamentRun() {}

    public TournamentRun(String tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getRunId() { return runId; }
    public void setRunId(String runId) { this.runId = runId; }

    public String getTournamentId() { return tournamentId; }
    public void setTournamentId(String tournamentId) { this.tournamentId = tournamentId; }

    public RunState getState() { return state; }
    public void setState(RunState state) { this.state = state; }

    public RaceLeg getCurrentLeg() { return currentLeg; }
    public void setCurrentLeg(RaceLeg currentLeg) { this.currentLeg = currentLeg; }

    public Instant getStartSignalTime() { return startSignalTime; }
    public void setStartSignalTime(Instant startSignalTime) { this.startSignalTime = startSignalTime; }

    public Map<Integer, Integer> getLaneFinishTimes() { return laneFinishTimes; }
    public void setLaneFinishTimes(Map<Integer, Integer> laneFinishTimes) { this.laneFinishTimes = laneFinishTimes; }

    public LegResult getLastLegResult() { return lastLegResult; }
    public void setLastLegResult(LegResult lastLegResult) { this.lastLegResult = lastLegResult; }
}
