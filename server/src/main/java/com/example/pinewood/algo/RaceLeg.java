package com.example.pinewood.algo;

import java.util.ArrayList;
import java.util.List;

public class RaceLeg {
    private int legNumber;
    private List<LaneAssignment> assignments = new ArrayList<>();

    public RaceLeg() {}

    public RaceLeg(int legNumber, List<LaneAssignment> assignments) {
        this.legNumber = legNumber;
        this.assignments = assignments;
    }

    public int getLegNumber() { return legNumber; }
    public void setLegNumber(int legNumber) { this.legNumber = legNumber; }

    public List<LaneAssignment> getAssignments() { return assignments; }
    public void setAssignments(List<LaneAssignment> assignments) { this.assignments = assignments; }
}
