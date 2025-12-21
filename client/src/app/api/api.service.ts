import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AlgoInfo, Racer, RunView, TournamentConfig, TrackConfig } from './models';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ApiService {
  constructor(private http: HttpClient) {}

  // Tracks
  listTracks(): Observable<TrackConfig[]> { return this.http.get<TrackConfig[]>('/api/tracks'); }
  saveTrack(t: TrackConfig): Observable<TrackConfig> {
    return t.id ? this.http.put<TrackConfig>(\`/api/tracks/\${t.id}\`, t) : this.http.post<TrackConfig>('/api/tracks', t);
  }
  deleteTrack(id: string) { return this.http.delete<void>(\`/api/tracks/\${id}\`); }

  // Racers
  listRacers(): Observable<Racer[]> { return this.http.get<Racer[]>('/api/racers'); }
  saveRacer(r: Racer): Observable<Racer> {
    return r.id ? this.http.put<Racer>(\`/api/racers/\${r.id}\`, r) : this.http.post<Racer>('/api/racers', r);
  }
  deleteRacer(id: string) { return this.http.delete<void>(\`/api/racers/\${id}\`); }

  // Tournaments
  listTournaments(): Observable<TournamentConfig[]> { return this.http.get<TournamentConfig[]>('/api/tournaments'); }
  listAlgorithms(): Observable<AlgoInfo[]> { return this.http.get<AlgoInfo[]>('/api/tournaments/algorithms'); }
  saveTournament(t: TournamentConfig): Observable<TournamentConfig> {
    return t.id ? this.http.put<TournamentConfig>(\`/api/tournaments/\${t.id}\`, t) : this.http.post<TournamentConfig>('/api/tournaments', t);
  }
  deleteTournament(id: string) { return this.http.delete<void>(\`/api/tournaments/\${id}\`); }

  // Runs
  startRun(tournamentId: string) { return this.http.post<{runId: string, tournamentId: string, state: string}>(\`/api/runs/start?tournamentId=\${tournamentId}\`, {}); }
  viewRun(runId: string): Observable<RunView> { return this.http.get<RunView>(\`/api/runs/\${runId}\`); }
  nextLeg(runId: string): Observable<RunView> { return this.http.post<RunView>(\`/api/runs/\${runId}/next-leg\`, {}); }
  ackStart(runId: string): Observable<RunView> { return this.http.post<RunView>(\`/api/runs/\${runId}/ack-start\`, {}); }
  signalStart(runId: string): Observable<RunView> { return this.http.post<RunView>(\`/api/runs/\${runId}/signal/start\`, {}); }
  signalFinish(runId: string, lane: number): Observable<RunView> { return this.http.post<RunView>(\`/api/runs/\${runId}/signal/finish/\${lane}\`, {}); }
  proceed(runId: string): Observable<RunView> { return this.http.post<RunView>(\`/api/runs/\${runId}/proceed\`, {}); }
  rerun(runId: string): Observable<RunView> { return this.http.post<RunView>(\`/api/runs/\${runId}/rerun\`, {}); }
}
