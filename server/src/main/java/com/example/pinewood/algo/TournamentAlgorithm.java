package com.example.pinewood.algo;

import com.example.pinewood.model.Racer;
import com.example.pinewood.model.laneCount;

import java.util.List;
import java.util.Map;

public interface TournamentAlgorithm {
    String id();
    String displayName();

    void setConfig(Map<String, Object> config);
    void setRacers(List<Racer> racers);
    void setTrack(laneCount trackConfig);

    int totalLegCount();
    int legsLeft();

    RaceLeg nextLeg();
    void recordLegResult(LegResult result);

    boolean isComplete();
    List<RacerRanking> rankings();
}
