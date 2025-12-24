package io.github.chariot.api;

import io.github.chariot.model.TournamentConfig;
import io.github.chariot.run.TournamentRunService;
import io.github.chariot.service.ConfigService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tournaments")
@CrossOrigin
public class TournamentController {
    private final ConfigService service;
    private final TournamentRunService runService;

    public TournamentController(ConfigService service, TournamentRunService runService) {
        this.service = service;
        this.runService = runService;
    }

    @GetMapping
    public List<TournamentConfig> list() { return service.listTournaments(); }

    @GetMapping("/{id}")
    public TournamentConfig get(@PathVariable String id) { return service.getTournament(id).orElseThrow(); }

    @PostMapping
    public TournamentConfig create(@Valid @RequestBody TournamentConfig t) { return service.upsertTournament(t); }

    @PutMapping("/{id}")
    public TournamentConfig update(@PathVariable String id, @Valid @RequestBody TournamentConfig t) {
        t.setId(id);
        return service.upsertTournament(t);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) { service.deleteTournament(id); }

    @GetMapping("/algorithms")
    public List<Map<String, String>> algorithms() { return runService.listAlgorithms(); }
}
