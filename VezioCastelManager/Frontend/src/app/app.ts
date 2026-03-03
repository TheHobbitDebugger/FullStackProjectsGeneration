import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TopMenu } from './top-menu/top-menu';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, TopMenu],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  // anno corrente calcolato dinamicamente — si aggiorna da solo ogni anno
  currentYear = new Date().getFullYear();
}
