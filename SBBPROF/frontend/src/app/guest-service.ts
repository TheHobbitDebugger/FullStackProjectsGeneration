import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Guest } from './model/hotel.entities';

@Injectable({
  providedIn: 'root',
})
export class GuestService {
  
    http = inject(HttpClient);

    searchApi = "http://localhost:8080/sbb/api/guests/bykey/";

    public findByKey(key:string):Observable<Guest[]>{
        return this.http.get<Guest[]>(this.searchApi+key);
    }

}
