import { Component, inject, model, signal } from '@angular/core';
import { Guest } from '../model/hotel.entities';
import { FormsModule } from '@angular/forms';
import { GuestService } from '../guest-service';

@Component({
  selector: 'app-guest-picker',
  imports: [FormsModule],
  templateUrl: './guest-picker.html',
  styleUrl: './guest-picker.css',
})
export class GuestPicker {

    guest = model.required<Guest | null>();
    key = signal<string>("");
    matches = signal<Guest[]>([]);

    guestService = inject(GuestService);
    errorMessage = signal<string>("");
    
   

    public doSearch():void{
        this.guestService.findByKey(this.key()).subscribe({
            next:matches=>{
                this.matches.set(matches);
                if(matches.length==1){
                    this.select(matches[0]);
                }
            },
            error:err=>this.errorMessage.set(err)
        });
    }

    public select(g:Guest){
        this.key.set(g.ssn+" "+g.firstName+" "+g.lastName+ " "+g.address+","+g.city);
        this.guest.set(g);
    }

    public clear(){
        this.guest.set(null);
        this.key.set("");
    }
    

}
