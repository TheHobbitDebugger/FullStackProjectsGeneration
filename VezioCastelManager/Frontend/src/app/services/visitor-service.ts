import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Visitor } from '../model/entities';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class VisitorService
{
  private url = 'http://localhost:8080/vcm/api/visitors';
  http = inject(HttpClient);

  // GET /visitors
  findAll(): Observable<Visitor[]>
  {
    const request = this.http.get<Visitor[]>(this.url);
    return request;
  }

  // POST /visitors — crea un nuovo visitatore
  save(visitor: Visitor): Observable<Visitor>
  {
    const request = this.http.post<Visitor>(this.url, visitor);
    return request;
  }
}
