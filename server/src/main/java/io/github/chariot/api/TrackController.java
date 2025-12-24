package io.github.chariot.api;

import io.github.chariot.model.TrackConfig;
import io.github.chariot.service.ConfigService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tracks")
@CrossOrigin
public class TrackController {
    private final ConfigService service;
    public TrackController(ConfigService service) { this.service = service; }

    @GetMapping
    public List<TrackConfig> list() { return service.listTracks(); }

    @GetMapping("/{id}")
    public TrackConfig get(@PathVariable String id) {
        return service.getTrack(id).orElseThrow();
    }

    @PostMapping
    public TrackConfig create(@Valid @RequestBody TrackConfig t) {
        return service.upsertTrack(t);
    }

    @PutMapping("/{id}")
    public TrackConfig update(@PathVariable String id, @Valid @RequestBody TrackConfig t) {
        t.setId(id);
        return service.upsertTrack(t);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) { service.deleteTrack(id); }
}
