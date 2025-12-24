package io.github.chariot.api;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.chariot.model.Racer;
import io.github.chariot.service.ConfigService;
import jakarta.validation.Valid;

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
