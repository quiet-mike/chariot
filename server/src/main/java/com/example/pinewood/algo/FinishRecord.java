package com.example.pinewood.algo;

public class FinishRecord {
    private String racerId;
    // hundredths of a second
    private int timeHundredths;
    private int place;

    public FinishRecord() {}

    public FinishRecord(String racerId, int timeHundredths, int place) {
        this.racerId = racerId;
        this.timeHundredths = timeHundredths;
        this.place = place;
    }

    public String getRacerId() { return racerId; }
    public void setRacerId(String racerId) { this.racerId = racerId; }

    public int getTimeHundredths() { return timeHundredths; }
    public void setTimeHundredths(int timeHundredths) { this.timeHundredths = timeHundredths; }

    public int getPlace() { return place; }
    public void setPlace(int place) { this.place = place; }
}
