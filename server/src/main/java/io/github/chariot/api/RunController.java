package io.github.chariot.api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.chariot.run.RunView;
import io.github.chariot.run.TournamentRun;
import io.github.chariot.run.TournamentRunService;

@RestController
@RequestMapping("/api/runs")
@CrossOrigin
public class RunController {
    private final TournamentRunService service;
    public RunController(TournamentRunService service) { this.service = service; }

    @PostMapping("/start")
    public TournamentRun start(@RequestParam String tournamentId) {
        return service.startRun(tournamentId);
    }

    @GetMapping("/{runId}")
    public RunView view(@PathVariable String runId) {
        return service.view(runId);
    }

    @PostMapping("/{runId}/next-leg")
    public RunView nextLeg(@PathVariable String runId) {
        return service.nextLeg(runId);
    }

    @PostMapping("/{runId}/ack-start")
    public RunView ackStart(@PathVariable String runId) { return service.clientAckStart(runId); }

    @PostMapping("/{runId}/signal/start")
    public RunView signalStart(@PathVariable String runId) { return service.signalStart(runId); }

    @PostMapping("/{runId}/signal/finish/{laneNumber}")
    public RunView signalFinish(@PathVariable String runId, @PathVariable int laneNumber) {
        return service.signalFinish(runId, laneNumber);
    }

    @PostMapping("/{runId}/proceed")
    public RunView proceed(@PathVariable String runId) { return service.proceed(runId); }

    @PostMapping("/{runId}/rerun")
    public RunView rerun(@PathVariable String runId) { return service.rerun(runId); }
}
