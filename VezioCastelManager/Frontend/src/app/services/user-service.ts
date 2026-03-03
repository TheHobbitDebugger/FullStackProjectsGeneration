import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { User } from '../model/entities';
import { Observable, tap } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root', // il servizio è singleton: esiste una sola istanza in tutta l'app
})
export class UserService
{
  http = inject(HttpClient);   // per fare chiamate HTTP al backend
  router = inject(Router);     // per navigare tra le pagine dopo login/logout

  // signal privato: solo questa classe può modificare l'utente loggato
  // all'avvio provo subito a recuperare l'utente dal localStorage
  private     _loggedUser = signal<User | null>(this.getUserFromStorage());

  // versione readonly esposta all'esterno: i componenti possono leggere ma non modificare
  public      loggedUser = this._loggedUser.asReadonly();

  // controllo se esiste un token salvato nel browser
  // se c'è, lo decodifico e restituisco l'utente; altrimenti null
  private getUserFromStorage(): User | null
  {
    const token = localStorage.getItem('token');
    if (!token)
      return null;
    return (this.tokenToUser(token)); // converto il token in oggetto User
  }

  // un JWT è composto da : header.payload.signature
  // la parte che ci interessa è il payload
  // atob() decodifica il Base64, poi JSON.parse() lo trasforma in oggetto
  private tokenToUser(token: string): User | null
  {
    try
    {
      const payload = JSON.parse(atob(token.split('.')[1]));
      // mappo i campi del payload all'interfaccia User definita nel modello
      return {
        id:        payload.id,
        firstName: payload.firstName,
        lastName:  payload.lastName,
        email:     payload.email,
        username:  payload.username
      };
    }
    catch (e)
    {
      // se il token è malformato o scaduto
      return null;
    }
  }

  // metodo pubblico chiamato dal componente di login
  // restituisce un Observable perché la chiamata HTTP è asincrona, il componente si occuperà di fare la subscribe
  public doLogin(username: string, password: string): Observable<any>
  {
    return this.http.post('http://localhost:8080/vcm/api/users/login', { username, password })
      .pipe(
        // tap() mi permette di fare "effetti collaterali" senza modificare il flusso dati
        // viene eseguito solo quando la risposta arriva con successo
        tap((json: any) => {
          const token = json.token; // estraggo il token dalla risposta
          localStorage.setItem('token', token); // salvo il token nel browser
          this._loggedUser.set(this.tokenToUser(token)); // aggiorno il signal con l'utente loggato
          this.router.navigate(['/ticket-form']); // reindirizzo al new ticket
        })
      );
  }

  // logout: pulisco tutto e rimando al login
  public doLogout(): void
  {
    localStorage.removeItem('token'); // rimuovo il token dal browser
    this._loggedUser.set(null);   // resetto il signal (nessun utente loggato)
    this.router.navigate(['/login']);  // reindirizzo alla pagina di login
  }
}
