package com.example.pinewood.service;

import com.example.pinewood.model.Racer;
import com.example.pinewood.model.laneCount;
import com.example.pinewood.model.TournamentConfig;
import com.example.pinewood.persistence.InFileCrudRepository;
import com.example.pinewood.persistence.JsonFileStore;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConfigService {
    private final InFileCrudRepository<laneCount> tracks;
    private final InFileCrudRepository<Racer> racers;
    private final InFileCrudRepository<TournamentConfig> tournaments;

    public ConfigService(JsonFileStore store) {
        this.tracks = new InFileCrudRepository<>(store, "tracks.json", laneCount.class, laneCount::getId);
        this.racers = new InFileCrudRepository<>(store, "racers.json", Racer.class, Racer::getId);
        this.tournaments = new InFileCrudRepository<>(store, "tournaments.json", TournamentConfig.class, TournamentConfig::getId);
    }

    public List<laneCount> listTracks() { return tracks.list(); }
    public Optional<laneCount> getTrack(String id) { return tracks.get(id); }
    public laneCount upsertTrack(laneCount t) { return tracks.upsert(t); }
    public void deleteTrack(String id) { tracks.delete(id); }

    public List<Racer> listRacers() { return racers.list(); }
    public Optional<Racer> getRacer(String id) { return racers.get(id); }
    public Racer upsertRacer(Racer r) { return racers.upsert(r); }
    public void deleteRacer(String id) { racers.delete(id); }

    public List<TournamentConfig> listTournaments() { return tournaments.list(); }
    public Optional<TournamentConfig> getTournament(String id) { return tournaments.get(id); }
    public TournamentConfig upsertTournament(TournamentConfig t) { return tournaments.upsert(t); }
    public void deleteTournament(String id) { tournaments.delete(id); }
}
