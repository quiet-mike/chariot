package com.example.pinewood.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class TrackLane {
    @Min(1)
    private int laneNumber;

    @NotBlank
    private String color;

    private boolean outOfService = false;

    public TrackLane() {}

    public TrackLane(int laneNumber, String color) {
        this.laneNumber = laneNumber;
        this.color = color;
    }

    public int getLaneNumber() { return laneNumber; }
    public void setLaneNumber(int laneNumber) { this.laneNumber = laneNumber; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public boolean isOutOfService() { return this.outOfService; }
    public void setOutOfService(boolean outOfService) { this.outOfService = outOfService; }
}
