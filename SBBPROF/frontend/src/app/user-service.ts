import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { User } from './model/hotel.entities';
import { Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
@Injectable({
  providedIn: 'root',
})
export class UserService {
  
    // cominciamo caricando un utente standard senza login 
    // solo per provare quello che devo provare

    http = inject(HttpClient);

    private _loggedUser = signal<User | null>(this.getUserFromStorage());
    loggedUser = this._loggedUser.asReadonly();
    router = inject(Router);


    constructor(){
    }

    private getUserFromStorage(): User | null {
        const token = localStorage.getItem('token');
        if (!token) return null;

        return this.tokenToUser(token);
    }

    private tokenToUser(token:string):User | null{
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            return {
                id: payload.id,
                firstName: payload.firstName,
                lastName: payload.lastName,
                email: payload.email,
                role: payload.role,
                hotel: {
                    id: payload.hotelId,
                    name: payload.hotelName,
                    address: payload.hotelAddress,
                    city: payload.hotelCity
                }
            };
        } catch (e) {
            return null;
        }
    }

    public doLogin(username:string, password:string):Observable<any>{
        return this.http.post("http://localhost:8080/sbb/api/users/login", {username:username, password:password})
        .pipe(tap((json:any) => {
                const token = json.token;
                // ci servirà dopo...
                localStorage.setItem('token', token);
                this._loggedUser.set(this.tokenToUser(token));
                console.log(this._loggedUser());
                this.router.navigate(['/']);
            })
        );
    }

    public doLogout():void{
        localStorage.removeItem('token');
        this._loggedUser.set(null);
        this.router.navigate(['/login']);
    }

}
