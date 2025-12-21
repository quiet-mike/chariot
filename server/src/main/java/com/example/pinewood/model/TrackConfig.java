package com.example.pinewood.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TrackConfig {
    private String id = UUID.randomUUID().toString();

    @NotBlank
    private String name;

    @Min(1)
    private int laneCount;

    @NotNull
    @Valid
    private List<TrackLane> lanes = new ArrayList<>();

    public TrackConfig() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getLaneCount() { return laneCount; }
    public void setLaneCount(int laneCount) { this.laneCount = laneCount; }

    public List<TrackLane> getLanes() { return lanes; }
    public void setLanes(List<TrackLane> lanes) { this.lanes = lanes; }
}
