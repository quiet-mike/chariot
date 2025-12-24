import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterLink, RouterOutlet],
  template: `
  <div class="container">
    <h1>Chariot</h1>
    <div class="nav card">
      <a routerLink="/tracks">Track Config</a>
      <a routerLink="/racers">Racers</a>
      <a routerLink="/tournaments">Tournaments</a>
      <a routerLink="/run">Run Tournament</a>
    </div>

    <router-outlet></router-outlet>

    <div *ngIf="isMenu" class="card">
      <h2>Main Menu</h2>
      <p class="small">Configure tracks, racers, tournaments, then run a tournament (mocked I/O signals).</p>
      <ol>
        <li>Create a Track with lane colors/numbers</li>
        <li>Create Racers</li>
        <li>Create a Tournament selecting the Track + Racers + algorithm</li>
        <li>Go to Run Tournament</li>
      </ol>
    </div>
  </div>
  `
})
export class AppComponent {
  get isMenu() {
    return location.pathname.endsWith('/menu') || location.pathname === '/' || location.pathname === '';
  }
}
