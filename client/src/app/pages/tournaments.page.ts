import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../api/api.service';
import { AlgoInfo, Racer, TournamentConfig, TrackConfig } from '../api/models';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
  <div class="card">
    <h2>Tournaments</h2>

    <div class="row">
      <div>
        <label>Name</label><br/>
        <input [(ngModel)]="draft.name" />
      </div>
      <div>
        <label>Track</label><br/>
        <select [(ngModel)]="draft.trackConfigId">
          <option [ngValue]="''">-- select --</option>
          <option *ngFor="let t of tracks" [ngValue]="t.id">{{t.name}}</option>
        </select>
      </div>
      <div>
        <label>Algorithm</label><br/>
        <select [(ngModel)]="draft.algorithmId">
          <option *ngFor="let a of algos" [ngValue]="a.id">{{a.name}}</option>
        </select>
      </div>
      <div>
        <label>Timeout (sec)</label><br/>
        <input type="number" [(ngModel)]="draft.timeoutSeconds" />
      </div>
    </div>

    <div class="card">
      <h3>Algorithm Config (JSON)</h3>
      <p class="small">Default algorithm supports: &#123;"rounds": 2&#125;</p>
      <textarea rows="4" style="width: 100%" [(ngModel)]="algoConfigText"></textarea>
      <div class="small" *ngIf="algoConfigError">{{algoConfigError}}</div>
    </div>

    <div class="card">
      <h3>Select Racers</h3>
      <div *ngFor="let r of racers">
        <label>
          <input type="checkbox" [checked]="selected(r.id!)" (change)="toggle(r.id!)" />
          {{r.firstName}} {{r.lastName}} (#{{r.number}})
        </label>
      </div>
    </div>

    <button (click)="save()">Save</button>
    <button (click)="reset()">New</button>
  </div>

  <div class="card">
    <h3>Existing Tournaments</h3>
    <table>
      <thead><tr><th>Name</th><th>Track</th><th>Racers</th><th></th></tr></thead>
      <tbody>
        <tr *ngFor="let t of tournaments">
          <td>{{t.name}}</td>
          <td>{{trackName(t.trackConfigId)}}</td>
          <td>{{t.racerIds.length}}</td>
          <td>
            <button (click)="edit(t)">Edit</button>
            <button (click)="del(t.id!)">Delete</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
  `
})
export class TournamentsPage {
  tracks: TrackConfig[] = [];
  racers: Racer[] = [];
  algos: AlgoInfo[] = [];
  tournaments: TournamentConfig[] = [];

  draft: TournamentConfig = {
    name: '',
    trackConfigId: '',
    racerIds: [],
    algorithmId: 'default-round-robin',
    algorithmConfig: { rounds: 2 },
    timeoutSeconds: 12
  };

  algoConfigText = JSON.stringify({ rounds: 2 }, null, 2);
  algoConfigError = '';

  constructor(private api: ApiService) {
    this.reset();
    this.reloadAll();
  }

  reloadAll() {
    this.api.listTracks().subscribe(x => this.tracks = x);
    this.api.listRacers().subscribe(x => this.racers = x);
    this.api.listAlgorithms().subscribe(x => this.algos = x);
    this.api.listTournaments().subscribe(x => this.tournaments = x);
  }

  trackName(id: string) {
    return this.tracks.find(t => t.id === id)?.name || id;
  }

  selected(id: string) { return this.draft.racerIds.includes(id); }

  toggle(id: string) {
    if (this.selected(id)) this.draft.racerIds = this.draft.racerIds.filter(x => x !== id);
    else this.draft.racerIds = [...this.draft.racerIds, id];
  }

  reset() {
    this.draft = {
      name: '',
      trackConfigId: '',
      racerIds: [],
      algorithmId: 'default-round-robin',
      algorithmConfig: { rounds: 2 },
      timeoutSeconds: 12
    };
    this.algoConfigText = JSON.stringify(this.draft.algorithmConfig, null, 2);
    this.algoConfigError = '';
  }

  edit(t: TournamentConfig) {
    this.draft = JSON.parse(JSON.stringify(t));
    this.algoConfigText = JSON.stringify(this.draft.algorithmConfig || {}, null, 2);
    this.algoConfigError = '';
  }

  save() {
    try {
      this.draft.algorithmConfig = JSON.parse(this.algoConfigText || '{}');
      this.algoConfigError = '';
    } catch (e: any) {
      this.algoConfigError = 'Invalid JSON';
      return;
    }
    this.api.saveTournament(this.draft).subscribe(() => { this.reset(); this.reloadAll(); });
  }

  del(id: string) { this.api.deleteTournament(id).subscribe(() => this.reloadAll()); }
}
