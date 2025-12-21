import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../api/api.service';
import { Racer } from '../api/models';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: \`
  <div class="card">
    <h2>Racers</h2>
    <div class="row">
      <div>
        <label>First Name</label><br/>
        <input [(ngModel)]="draft.firstName" />
      </div>
      <div>
        <label>Last Name</label><br/>
        <input [(ngModel)]="draft.lastName" />
      </div>
      <div>
        <label>Number</label><br/>
        <input [(ngModel)]="draft.number" />
      </div>
      <div>
        <label>Car Name (optional)</label><br/>
        <input [(ngModel)]="draft.carName" />
      </div>
    </div>
    <button (click)="save()">Save</button>
    <button (click)="reset()">New</button>
  </div>

  <div class="card">
    <h3>Existing Racers</h3>
    <table>
      <thead><tr><th>Name</th><th>#</th><th>Car</th><th></th></tr></thead>
      <tbody>
        <tr *ngFor="let r of racers">
          <td>{{r.firstName}} {{r.lastName}}</td>
          <td>{{r.number}}</td>
          <td>{{r.carName || ''}}</td>
          <td>
            <button (click)="edit(r)">Edit</button>
            <button (click)="del(r.id!)">Delete</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
  \`
})
export class RacersPage {
  racers: Racer[] = [];
  draft: Racer = { firstName: '', lastName: '', number: '', carName: '' };

  constructor(private api: ApiService) { this.reset(); this.reload(); }

  reload() { this.api.listRacers().subscribe(x => this.racers = x); }
  reset() { this.draft = { firstName: '', lastName: '', number: '', carName: '' }; }

  edit(r: Racer) { this.draft = JSON.parse(JSON.stringify(r)); }
  save() { this.api.saveRacer(this.draft).subscribe(() => { this.reset(); this.reload(); }); }
  del(id: string) { this.api.deleteRacer(id).subscribe(() => this.reload()); }
}
