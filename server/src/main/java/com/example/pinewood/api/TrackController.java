package com.example.pinewood.api;

import com.example.pinewood.model.laneCount;
import com.example.pinewood.service.ConfigService;

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
    public List<laneCount> list() { return service.listTracks(); }

    @GetMapping("/{id}")
    public laneCount get(@PathVariable String id) {
        return service.getTrack(id).orElseThrow();
    }

    @PostMapping
    public laneCount create(@Valid @RequestBody laneCount t) {
        return service.upsertTrack(t);
    }

    @PutMapping("/{id}")
    public laneCount update(@PathVariable String id, @Valid @RequestBody laneCount t) {
        t.setId(id);
        return service.upsertTrack(t);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) { service.deleteTrack(id); }
}
