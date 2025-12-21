package com.example.pinewood.algo;

public class LaneAssignment {
    private int laneNumber;
    private String laneColor;
    private String racerId;

    public LaneAssignment() {}

    public LaneAssignment(int laneNumber, String laneColor, String racerId) {
        this.laneNumber = laneNumber;
        this.laneColor = laneColor;
        this.racerId = racerId;
    }

    public int getLaneNumber() { return laneNumber; }
    public void setLaneNumber(int laneNumber) { this.laneNumber = laneNumber; }

    public String getLaneColor() { return laneColor; }
    public void setLaneColor(String laneColor) { this.laneColor = laneColor; }

    public String getRacerId() { return racerId; }
    public void setRacerId(String racerId) { this.racerId = racerId; }
}
