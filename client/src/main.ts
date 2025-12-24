import 'zone.js';
import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient } from '@angular/common/http';
import { provideRouter, Routes } from '@angular/router';
import { AppComponent } from './app/app.component';
import { TracksPage } from './app/pages/tracks.page';
import { RacersPage } from './app/pages/racers.page';
import { TournamentsPage } from './app/pages/tournaments.page';
import { RunPage } from './app/pages/run.page';

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'menu' },
  { path: 'menu', component: AppComponent },
  { path: 'tracks', component: TracksPage },
  { path: 'racers', component: RacersPage },
  { path: 'tournaments', component: TournamentsPage },
  { path: 'run', component: RunPage }
];

bootstrapApplication(AppComponent, {
  providers: [
    provideHttpClient(),
    provideRouter(routes)
  ]
});
