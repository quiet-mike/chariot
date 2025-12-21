package com.example.pinewood.api;

import com.example.pinewood.run.RunView;
import com.example.pinewood.run.TournamentRun;
import com.example.pinewood.run.TournamentRunService;
import org.springframework.web.bind.annotation.*;

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
