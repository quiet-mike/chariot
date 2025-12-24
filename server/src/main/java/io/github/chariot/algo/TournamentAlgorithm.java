package io.github.chariot.algo;

import java.util.List;
import java.util.Map;

import io.github.chariot.model.Racer;
import io.github.chariot.model.TrackConfig;

public interface TournamentAlgorithm {
    String id();
    String displayName();

    void setConfig(Map<String, Object> config);
    void setRacers(List<Racer> racers);
    void setTrack(TrackConfig trackConfig);

    int totalLegCount();
    int legsLeft();

    RaceLeg nextLeg();
    void recordLegResult(LegResult result);

    boolean isComplete();
    List<RacerRanking> rankings();
}
