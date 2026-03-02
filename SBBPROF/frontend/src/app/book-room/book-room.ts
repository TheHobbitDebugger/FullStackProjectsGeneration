import { Component, computed, effect, inject, signal, WritableSignal } from '@angular/core';
import { UserService } from '../user-service';
import { RoomService } from '../room-service';
import { Guest, Room, RoomCategory } from '../model/hotel.entities';
import { FormsModule } from '@angular/forms';
import { GuestPicker } from '../guest-picker/guest-picker';
import { RoomPicker } from '../room-picker/room-picker';
import { RoomWithCategory } from '../model/wrapper';




@Component({
  selector: 'app-book-room',
  imports: [RoomPicker, FormsModule, GuestPicker],
  templateUrl: './book-room.html',
  styleUrl: './book-room.css',
})
export class BookRoom {
    userService = inject(UserService);
    roomService = inject(RoomService);
    loggedUser = this.userService.loggedUser;
    
    // stato della prenotazione suddiviso in signal
    from = signal<Date>(new Date());
    to = signal<Date>(new Date(new Date().setDate(new Date().getDate() + 1)));
    guest = signal<Guest | null>(null);
    price = signal<number |null>(null);
    notes = signal<string>("");
    freeRoomsByCategories = signal<RoomCategory[]>([]);
    selectedRoomWithCategory = signal<RoomWithCategory | null>(null);
    room = signal<Room | null>(null);

    // etichetta 
    selectedCategoryLabel = computed<string>(()=>{
        if(this.selectedRoomWithCategory()==null)
            return "-";
        return  this.selectedRoomWithCategory()!.roomName
                +" - "+this.selectedRoomWithCategory()!.roomCategory.name+", "    
                +this.selectedRoomWithCategory()!.roomCategory.price+ " euro";
    });


    // abbiamo cambiato le date... ricarico le stanze libere
    loadFreeRoomsByCategory = effect(()=>{
        console.log("RIESEGUO");
        if(this.loggedUser()==null || this.to()==null || this.from()==null)
            return;
    
        let id:number = this.loggedUser()?.hotel.id ?? 0;


        this.roomService.findFreeRoomsForHotel(id, this.from(), this.to()).subscribe({
            next:(categories)=>{
                this.freeRoomsByCategories.set(categories);
                let category = categories[0];
                if(category!=null){
                    let res: RoomWithCategory = {roomCategory:category, roomName:category.defaultRoom};
                    this.selectedRoomWithCategory.set(res);
                }
            },
            error:(error)=>{console.log(error)}
        });
    });
   
    updatePrice = effect(()=>{
        if(this.selectedRoomWithCategory()==null)
            return;
        this.price.set(this.selectedRoomWithCategory()!.roomCategory.price);
    });

    valid = computed(()=> this.price()!>0 );

    book():void{

    }
    
}
