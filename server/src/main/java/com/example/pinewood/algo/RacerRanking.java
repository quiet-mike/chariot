package com.example.pinewood.algo;

public class RacerRanking {
    private String racerId;
    private int rank;
    private int totalTimeHundredths;

    public RacerRanking() {}

    public RacerRanking(String racerId, int rank, int totalTimeHundredths) {
        this.racerId = racerId;
        this.rank = rank;
        this.totalTimeHundredths = totalTimeHundredths;
    }

    public String getRacerId() { return racerId; }
    public void setRacerId(String racerId) { this.racerId = racerId; }

    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }

    public int getTotalTimeHundredths() { return totalTimeHundredths; }
    public void setTotalTimeHundredths(int totalTimeHundredths) { this.totalTimeHundredths = totalTimeHundredths; }
}
