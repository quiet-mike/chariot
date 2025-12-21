import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../api/api.service';
import { TrackConfig, TrackSlot } from '../api/models';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: \`
  <div class="card">
    <h2>Track Configuration</h2>
    <div class="row">
      <div>
        <label>Name</label><br/>
        <input [(ngModel)]="draft.name" />
      </div>
      <div>
        <label>Slot Count</label><br/>
        <input type="number" [(ngModel)]="draft.slotCount" (change)="syncSlots()" />
      </div>
    </div>

    <div class="card">
      <h3>Lanes</h3>
      <div *ngFor="let s of draft.slots; let i = index" class="row">
        <div>
          <label>Lane #</label><br/>
          <input type="number" [(ngModel)]="draft.slots[i].laneNumber" />
        </div>
        <div>
          <label>Color</label><br/>
          <input [(ngModel)]="draft.slots[i].color" placeholder="red / blue / ..." />
        </div>
      </div>
    </div>

    <button (click)="save()">Save</button>
    <button (click)="reset()">New</button>
  </div>

  <div class="card">
    <h3>Existing Tracks</h3>
    <table>
      <thead><tr><th>Name</th><th>Slots</th><th></th></tr></thead>
      <tbody>
        <tr *ngFor="let t of tracks">
          <td>{{t.name}}</td>
          <td>{{t.slotCount}}</td>
          <td>
            <button (click)="edit(t)">Edit</button>
            <button (click)="del(t.id!)">Delete</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
  \`
})
export class TracksPage {
  tracks: TrackConfig[] = [];
  draft: TrackConfig = { name: '', slotCount: 4, slots: [] };

  constructor(private api: ApiService) { this.reset(); this.reload(); }

  syncSlots() {
    const n = Math.max(1, Number(this.draft.slotCount || 1));
    while (this.draft.slots.length < n) {
      const idx = this.draft.slots.length + 1;
      this.draft.slots.push({ laneNumber: idx, color: '' });
    }
    while (this.draft.slots.length > n) this.draft.slots.pop();
  }

  reload() { this.api.listTracks().subscribe(x => this.tracks = x); }

  reset() {
    this.draft = { name: '', slotCount: 4, slots: [] };
    this.syncSlots();
  }

  edit(t: TrackConfig) {
    this.draft = JSON.parse(JSON.stringify(t));
    this.syncSlots();
  }

  save() {
    this.api.saveTrack(this.draft).subscribe(() => { this.reset(); this.reload(); });
  }

  del(id: string) { this.api.deleteTrack(id).subscribe(() => this.reload()); }
}
