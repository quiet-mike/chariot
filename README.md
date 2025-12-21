# Pinewood Derby Race (Client/Server)

This repository contains:
- **server/**: Java 17 + Spring Boot REST API
- **client/**: Angular web client (skeleton UI)

It implements the requirements in `pinewood_derby_requirements.txt` by providing:
- Track, Racer, Tournament configuration CRUD (persisted as JSON on disk)
- Tournament algorithm API (default: simple round-robin-by-lane rotation)
- Tournament runner state machine with a **mock communications module** (start + finish lane signals)
- Angular pages for configuration and tournament run flow (ack start, simulate start signal, simulate finish signals, next/re-run)

> Note: The "communications module" is mocked via REST endpoints/buttons in the UI.

## Run the server

Prereqs: Java 17, Maven

```bash
cd server
mvn spring-boot:run
```

Server runs at `http://localhost:8080`.

Data is stored under `server/data/` as JSON.

## Run the client (dev)

Prereqs: Node 18+, Angular CLI

```bash
cd client
npm install
npm start
```

Client runs at `http://localhost:4200` and proxies API calls to `http://localhost:8080`.

## High-level API

- `GET/POST/PUT/DELETE /api/tracks`
- `GET/POST/PUT/DELETE /api/racers`
- `GET/POST/PUT/DELETE /api/tournaments`

Tournament run:
- `POST /api/runs/start?tournamentId=...` -> creates a run and returns `runId`
- `GET /api/runs/{runId}` -> get current state (next leg / results / rankings when complete)
- `POST /api/runs/{runId}/ack-start` -> client acknowledges okay to start
- `POST /api/runs/{runId}/signal/start` -> **mock hardware** start signal
- `POST /api/runs/{runId}/signal/finish/{laneNumber}` -> **mock hardware** finish signal for a lane
- `POST /api/runs/{runId}/proceed` -> accept leg results and advance
- `POST /api/runs/{runId}/rerun` -> rerun current leg

## Default Algorithm

`DefaultRoundRobinAlgorithm`:
- Determines legs by rotating racers through lanes to ensure each racer runs multiple times (configurable).
- Produces a sequence of legs where each leg contains up to `track.slotCount` racers.
- Ranking is computed from **total time** across legs (lowest wins).
