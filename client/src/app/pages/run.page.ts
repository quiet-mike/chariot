import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../api/api.service';
import { RunView, TournamentConfig } from '../api/models';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: \`
  <div class="card">
    <h2>Run Tournament</h2>

    <div class="row">
      <div>
        <label>Tournament</label><br/>
        <select [(ngModel)]="selectedTournamentId">
          <option [ngValue]="''">-- select --</option>
          <option *ngFor="let t of tournaments" [ngValue]="t.id">{{t.name}}</option>
        </select>
      </div>
      <div style="align-self:end">
        <button (click)="start()" [disabled]="!selectedTournamentId">Start Run</button>
      </div>
    </div>

    <div *ngIf="run" class="card">
      <div class="small">Run ID: {{run.runId}} • State: {{run.state}}</div>

      <div *ngIf="run.state === 'READY_FOR_NEXT_LEG'">
        <button (click)="nextLeg()">Get Next Leg</button>
      </div>

      <div *ngIf="run.currentLeg" class="card">
        <h3>Leg #{{run.currentLeg.legNumber}}</h3>

        <table>
          <thead><tr><th>Lane</th><th>Color</th><th>Racer</th><th>Mock Finish</th></tr></thead>
          <tbody>
            <tr *ngFor="let r of run.currentLegRacers">
              <td>{{r.laneNumber}}</td>
              <td>{{r.laneColor}} <span class="badge">{{r.laneColor}}</span></td>
              <td>{{r.displayName}}</td>
              <td><button (click)="finish(r.laneNumber)" [disabled]="run.state !== 'RUNNING_LEG'">Finish</button></td>
            </tr>
          </tbody>
        </table>

        <div class="row">
          <button (click)="ackStart()" [disabled]="run.state !== 'AWAITING_CLIENT_ACK_START'">Acknowledge Start</button>
          <button (click)="signalStart()" [disabled]="run.state !== 'AWAITING_START_SIGNAL'">Mock Start Signal</button>
          <button (click)="refresh()">Refresh</button>
        </div>
      </div>

      <div *ngIf="run.state === 'LEG_COMPLETE_AWAITING_CLIENT_DECISION'" class="card">
        <h3>Leg Results</h3>
        <pre>{{run.lastLegResult | json}}</pre>
        <button (click)="proceed()">Proceed to Next Leg</button>
        <button (click)="rerun()">Re-run This Leg</button>
      </div>

      <div *ngIf="run.state === 'TOURNAMENT_COMPLETE'" class="card">
        <h3>Final Rankings</h3>
        <table>
          <thead><tr><th>Rank</th><th>Racer ID</th><th>Total Time (hundredths)</th></tr></thead>
          <tbody>
            <tr *ngFor="let rr of run.rankings">
              <td>{{rr.rank}}</td>
              <td>{{rr.racerId}}</td>
              <td>{{rr.totalTimeHundredths}}</td>
            </tr>
          </tbody>
        </table>
      </div>

    </div>
  </div>
  \`
})
export class RunPage {
  tournaments: TournamentConfig[] = [];
  selectedTournamentId = '';
  run?: RunView;

  constructor(private api: ApiService) {
    this.api.listTournaments().subscribe(x => this.tournaments = x);
  }

  start() {
    this.api.startRun(this.selectedTournamentId).subscribe(r => {
      const runId = (r as any).runId;
      this.api.viewRun(runId).subscribe(v => this.run = v);
    });
  }

  refresh() {
    if (!this.run) return;
    this.api.viewRun(this.run.runId).subscribe(v => this.run = v);
  }

  nextLeg() {
    if (!this.run) return;
    this.api.nextLeg(this.run.runId).subscribe(v => this.run = v);
  }

  ackStart() {
    if (!this.run) return;
    this.api.ackStart(this.run.runId).subscribe(v => this.run = v);
  }

  signalStart() {
    if (!this.run) return;
    this.api.signalStart(this.run.runId).subscribe(v => this.run = v);
  }

  finish(lane: number) {
    if (!this.run) return;
    this.api.signalFinish(this.run.runId, lane).subscribe(v => this.run = v);
  }

  proceed() {
    if (!this.run) return;
    this.api.proceed(this.run.runId).subscribe(v => this.run = v);
  }

  rerun() {
    if (!this.run) return;
    this.api.rerun(this.run.runId).subscribe(v => this.run = v);
  }
}
