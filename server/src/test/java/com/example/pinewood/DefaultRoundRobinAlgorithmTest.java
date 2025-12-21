package com.example.pinewood;

import com.example.pinewood.algo.DefaultRoundRobinAlgorithm;
import com.example.pinewood.algo.LegResult;
import com.example.pinewood.algo.RaceLeg;
import com.example.pinewood.algo.FinishRecord;
import com.example.pinewood.model.Racer;
import com.example.pinewood.model.TrackConfig;
import com.example.pinewood.model.TrackLane;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultRoundRobinAlgorithmTest {
    @Test
    void generatesLegsAndRanksByTotalTime() {
        TrackConfig track = new TrackConfig();
        track.setName("4-lane");
        track.setSlotCount(4);
        track.setSlots(List.of(new TrackLane(1, "red"),
                new TrackLane(2, "blue"),
                new TrackLane(3, "green"),
                new TrackLane(4, "yellow")
        ));

        Racer a = new Racer(); a.setFirstName("A"); a.setLastName("A"); a.setNumber("1");
        Racer b = new Racer(); b.setFirstName("B"); b.setLastName("B"); b.setNumber("2");
        Racer c = new Racer(); c.setFirstName("C"); c.setLastName("C"); c.setNumber("3");
        Racer d = new Racer(); d.setFirstName("D"); d.setLastName("D"); d.setNumber("4");

        DefaultRoundRobinAlgorithm algo = new DefaultRoundRobinAlgorithm();
        algo.setConfig(Map.of("rounds", 1));
        algo.setRacers(List.of(a,b,c,d));
        algo.setTrack(track);

        assertEquals(1, algo.totalLegCount());
        RaceLeg leg = algo.nextLeg();
        assertEquals(4, leg.getAssignments().size());

        // Make A win
        LegResult lr = new LegResult(leg.getLegNumber(), List.of(
                new FinishRecord(a.getId(), 120, 1),
                new FinishRecord(b.getId(), 130, 2),
                new FinishRecord(c.getId(), 140, 3),
                new FinishRecord(d.getId(), 150, 4)
        ));
        algo.recordLegResult(lr);

        assertTrue(algo.isComplete());
        assertEquals(a.getId(), algo.rankings().get(0).getRacerId());
    }
}
