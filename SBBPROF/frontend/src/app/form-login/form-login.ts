import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../user-service';

@Component({
  selector: 'app-form-login',
  imports: [FormsModule],
  templateUrl: './form-login.html',
  styleUrl: './form-login.css'
})
export class FormLogin {
    userService = inject(UserService);
    router = inject(Router);

    username = signal<string>("");
    password = signal<string>("");
    errorMessage = signal<string>("");

    login(){
        this.errorMessage.set(""); // Reset errore precedente
        this.userService.doLogin(this.username(), this.password()).subscribe({
            next:()=>{
                // Login riuscito, vado alla home
                this.router.navigate(['/']);
            },
            error:(err)=>{
                console.log(err);
                this.errorMessage.set("Login fallito: controlla le credenziali.");
            }
        })
    }
}
