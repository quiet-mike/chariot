package com.example.pinewood.run;

import com.example.pinewood.algo.RaceLeg;
import com.example.pinewood.algo.RacerRanking;
import com.example.pinewood.model.Racer;

import java.util.ArrayList;
import java.util.List;

public class RunView {
    private String runId;
    private RunState state;

    private RaceLeg currentLeg;

    // Human-friendly expansions for UI
    private List<RacerSummary> currentLegRacers = new ArrayList<>();

    private Object lastLegResult; // use raw JSON object structure from algo.LegResult
    private List<RacerRanking> rankings = new ArrayList<>();

    public String getRunId() { return runId; }
    public void setRunId(String runId) { this.runId = runId; }

    public RunState getState() { return state; }
    public void setState(RunState state) { this.state = state; }

    public RaceLeg getCurrentLeg() { return currentLeg; }
    public void setCurrentLeg(RaceLeg currentLeg) { this.currentLeg = currentLeg; }

    public List<RacerSummary> getCurrentLegRacers() { return currentLegRacers; }
    public void setCurrentLegRacers(List<RacerSummary> currentLegRacers) { this.currentLegRacers = currentLegRacers; }

    public Object getLastLegResult() { return lastLegResult; }
    public void setLastLegResult(Object lastLegResult) { this.lastLegResult = lastLegResult; }

    public List<RacerRanking> getRankings() { return rankings; }
    public void setRankings(List<RacerRanking> rankings) { this.rankings = rankings; }

    public static class RacerSummary {
        private String racerId;
        private String displayName;
        private int laneNumber;
        private String laneColor;

        public RacerSummary() {}

        public RacerSummary(Racer r, int laneNumber, String laneColor) {
            this.racerId = r.getId();
            this.displayName = r.displayName();
            this.laneNumber = laneNumber;
            this.laneColor = laneColor;
        }

        public String getRacerId() { return racerId; }
        public void setRacerId(String racerId) { this.racerId = racerId; }

        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }

        public int getLaneNumber() { return laneNumber; }
        public void setLaneNumber(int laneNumber) { this.laneNumber = laneNumber; }

        public String getLaneColor() { return laneColor; }
        public void setLaneColor(String laneColor) { this.laneColor = laneColor; }
    }
}
