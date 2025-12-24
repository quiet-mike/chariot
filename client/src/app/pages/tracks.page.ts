import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../api/api.service';
import { TrackConfig, TrackLane } from '../api/models';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
  <div class="card">
    <h2>Track Configuration</h2>
    <div class="row">
      <div>
        <label>Name</label><br/>
        <input [(ngModel)]="draft.name" />
      </div>
      <div>
        <label>Lane Count</label><br/>
        <input type="number" [(ngModel)]="draft.laneCount" (change)="syncLanes()" />
      </div>
    </div>

    <div class="card">
      <h3>Lanes</h3>
      <div *ngFor="let s of draft.lanes; let i = index" class="row">
        <div>
          <label>Lane #</label><br/>
          <input type="number" [(ngModel)]="draft.lanes[i].laneNumber" />
        </div>
        <div>
          <label>Color</label><br/>
          <input [(ngModel)]="draft.lanes[i].color" placeholder="red / blue / ..." />
        </div>
      </div>
    </div>

    <button (click)="save()">Save</button>
    <button (click)="reset()">New</button>
  </div>

  <div class="card">
    <h3>Existing Tracks</h3>
    <table>
      <thead><tr><th>Name</th><th>Lanes</th><th></th></tr></thead>
      <tbody>
        <tr *ngFor="let t of tracks">
          <td>{{t.name}}</td>
          <td>{{t.laneCount}}</td>
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
export class TracksPage {
  tracks: TrackConfig[] = [];
  draft: TrackConfig = { name: '', laneCount: 4, lanes: [] };

  constructor(private api: ApiService) { this.reset(); this.reload(); }

  syncLanes() {
    const n = Math.max(1, Number(this.draft.laneCount || 1));
    while (this.draft.lanes.length < n) {
      const idx = this.draft.lanes.length + 1;
      this.draft.lanes.push({ laneNumber: idx, color: '' });
    }
    while (this.draft.lanes.length > n) this.draft.lanes.pop();
  }

  reload() { this.api.listTracks().subscribe(x => this.tracks = x); }

  reset() {
    this.draft = { name: '', laneCount: 4, lanes: [] };
    this.syncLanes();
  }

  edit(t: TrackConfig) {
    this.draft = JSON.parse(JSON.stringify(t));
    this.syncLanes();
  }

  save() {
    this.api.saveTrack(this.draft).subscribe(() => { this.reset(); this.reload(); });
  }

  del(id: string) { this.api.deleteTrack(id).subscribe(() => this.reload()); }
}
