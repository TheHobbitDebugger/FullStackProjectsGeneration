import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { RoomCategory } from './model/hotel.entities';
import { Observable } from 'rxjs';

/*
    per comodità qui mi occupo sia di Room propriamente dette che di Room Category
*/

@Injectable({
  providedIn: 'root',
})
export class RoomService {
    
    http = inject(HttpClient);
    freeRoomsUrl = "http://localhost:8080/sbb/api/rooms/free/";
    
  
    public findFreeRoomsForHotel(id:number, from:any, to:any):Observable<RoomCategory[]>{

        console.log(from);
        console.log(to);


        if(from.toISOString)
            from = from.toISOString().split("T")[0];
        if(to.toISOString)
            to = to.toISOString().split("T")[0];


        let url = this.freeRoomsUrl+id+"/"+from+"/"+to;
        // normalmente dovrei specificare gli header con Bearer e JWT...
        // per evitare di farlo sempre c'è qualcosa di simile ai filter in Spring
        // ma qui si chiamano interceptor
        // il filter di spring lavora sul server sulla request che arriva alle api
        // l'interceptor di angular lavora sulla request prima che venga inviata al server
        // il filter può modificare la request o fare altre azioni sul server
        // l'interceptor serve tendenzialmente a MODIFICARE LA REQUEST
        return this.http.get<RoomCategory[]>(url);
    }

}
