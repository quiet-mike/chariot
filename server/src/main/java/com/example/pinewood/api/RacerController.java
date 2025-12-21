package com.example.pinewood.api;

import com.example.pinewood.model.Racer;
import com.example.pinewood.service.ConfigService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/racers")
@CrossOrigin
public class RacerController {
    private final ConfigService service;
    public RacerController(ConfigService service) { this.service = service; }

    @GetMapping
    public List<Racer> list() { return service.listRacers(); }

    @GetMapping("/{id}")
    public Racer get(@PathVariable String id) {
        return service.getRacer(id).orElseThrow();
    }

    @PostMapping
    public Racer create(@Valid @RequestBody Racer r) { return service.upsertRacer(r); }

    @PutMapping("/{id}")
    public Racer update(@PathVariable String id, @Valid @RequestBody Racer r) {
        r.setId(id);
        return service.upsertRacer(r);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) { service.deleteRacer(id); }
}
