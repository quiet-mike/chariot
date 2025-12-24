package io.github.chariot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import io.github.chariot.model.Racer;
import io.github.chariot.model.TournamentConfig;
import io.github.chariot.model.TrackConfig;
import io.github.chariot.persistence.InFileCrudRepository;
import io.github.chariot.persistence.JsonFileStore;

@Service
public class ConfigService {
    private final InFileCrudRepository<TrackConfig> tracks;
    private final InFileCrudRepository<Racer> racers;
    private final InFileCrudRepository<TournamentConfig> tournaments;

    public ConfigService(JsonFileStore store) {
        this.tracks = new InFileCrudRepository<>(store, "tracks.json", TrackConfig.class, TrackConfig::getId);
        this.racers = new InFileCrudRepository<>(store, "racers.json", Racer.class, Racer::getId);
        this.tournaments = new InFileCrudRepository<>(store, "tournaments.json", TournamentConfig.class, TournamentConfig::getId);
    }

    public List<TrackConfig> listTracks() { return tracks.list(); }
    public Optional<TrackConfig> getTrack(String id) { return tracks.get(id); }
    public TrackConfig upsertTrack(TrackConfig t) { return tracks.upsert(t); }
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
