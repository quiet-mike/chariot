package com.example.pinewood.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.*;

public class TournamentConfig {
    private String id = UUID.randomUUID().toString();

    @NotBlank
    private String name;

    @NotBlank
    private String trackConfigId;

    @NotEmpty
    private List<String> racerIds = new ArrayList<>();

    @NotBlank
    private String algorithmId = "default-round-robin";

    // algorithm configuration as key/value pairs (persisted as JSON)
    private Map<String, Object> algorithmConfig = new HashMap<>();

    @Min(1)
    private int timeoutSeconds = 12;

    public TournamentConfig() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTrackConfigId() { return trackConfigId; }
    public void setTrackConfigId(String trackConfigId) { this.trackConfigId = trackConfigId; }

    public List<String> getRacerIds() { return racerIds; }
    public void setRacerIds(List<String> racerIds) { this.racerIds = racerIds; }

    public String getAlgorithmId() { return algorithmId; }
    public void setAlgorithmId(String algorithmId) { this.algorithmId = algorithmId; }

    public Map<String, Object> getAlgorithmConfig() { return algorithmConfig; }
    public void setAlgorithmConfig(Map<String, Object> algorithmConfig) { this.algorithmConfig = algorithmConfig; }

    public int getTimeoutSeconds() { return timeoutSeconds; }
    public void setTimeoutSeconds(int timeoutSeconds) { this.timeoutSeconds = timeoutSeconds; }
}
