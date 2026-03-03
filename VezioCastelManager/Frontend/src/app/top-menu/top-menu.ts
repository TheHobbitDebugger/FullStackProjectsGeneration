import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { UserService } from '../services/user-service';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-top-menu',
  imports: [RouterLink, MatIconModule],
  templateUrl: './top-menu.html',
  styleUrl: './top-menu.css',
})
export class TopMenu {

  // inietto UserService per leggere l'utente loggato e fare il logout
  userService = inject(UserService);

  // loggedUser è un signal readonly: quando cambia (login/logout),
  // Angular aggiorna automaticamente il template senza fare nulla manualmente
  loggedUser = this.userService.loggedUser;

  // delego il logout al service, che pulisce il token e reindirizza al login
  logout(): void {
    this.userService.doLogout();
  }
}
