import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../services/user-service';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-login-form',
  imports: [FormsModule, MatCardModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatIconModule],
  templateUrl: './login-form.html',
  styleUrl: './login-form.css'
})
export class LoginForm {

  // userService gestisce la chiamata HTTP e salva il token nel localStorage
  userService  = inject(UserService);
  router       = inject(Router);

  // uso i signal per i campi del form: ogni modifica aggiorna il valore reattivamente
  username     = signal<string>('');
  password     = signal<string>('');
  errorMessage = signal<string>('');

  login(): void {
    this.errorMessage.set(''); // pulisco eventuali errori precedenti prima di riprovare

    // doLogin() restituisce un Observable: la chiamata HTTP parte solo quando faccio subscribe
    this.userService.doLogin(this.username(), this.password()).subscribe({
      next: () => {
        // il service si occupa già di salvare il token e navigare — qui non devo fare nulla
        this.router.navigate(['/']);
      },
      error: (err) => {
        // mostro il messaggio di errore solo in caso di credenziali errate o server down
        console.log(err);
        this.errorMessage.set('Login fallito: controlla le credenziali.');
      }
    });
  }
}
