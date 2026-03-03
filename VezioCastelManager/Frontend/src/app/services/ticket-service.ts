import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Ticket } from '../model/entities';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class TicketService
{
  private url = 'http://localhost:8080/vcm/api/tickets';
  http = inject(HttpClient);

  // GET /tickets
  findAll(): Observable<Ticket[]>
  {
    const request = this.http.get<Ticket[]>(this.url);
    return request;
  }

  // POST /tickets — il prezzo NON va mandato: il backend lo calcola in base all'età del visitatore
  save(ticket: Ticket): Observable<Ticket>
  {
    const request = this.http.post<Ticket>(this.url, ticket);
    return request;
  }

  // DELETE /tickets/{id} — elimina un biglietto
  delete(id: number): Observable<void>
  {
    const endpoint = `${this.url}/${id}`;
    const request  = this.http.delete<void>(endpoint);
    return request;
  }
}
