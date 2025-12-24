package io.github.chariot.algo;

import java.util.ArrayList;
import java.util.List;

public class LegResult {
    private int legNumber;
    // ordered: winner first
    private List<FinishRecord> finishes = new ArrayList<>();

    public LegResult() {}

    public LegResult(int legNumber, List<FinishRecord> finishes) {
        this.legNumber = legNumber;
        this.finishes = finishes;
    }

    public int getLegNumber() { return legNumber; }
    public void setLegNumber(int legNumber) { this.legNumber = legNumber; }

    public List<FinishRecord> getFinishes() { return finishes; }
    public void setFinishes(List<FinishRecord> finishes) { this.finishes = finishes; }
}
