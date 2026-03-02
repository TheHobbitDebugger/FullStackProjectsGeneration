import { Component, input, model, signal } from '@angular/core';
import { RoomCategory } from '../model/hotel.entities';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { RoomWithCategory } from '../model/wrapper';


@Component({
  selector: 'app-room-picker',
  imports: [FormsModule],
  templateUrl: './room-picker.html',
  styleUrl: './room-picker.css',
})
export class RoomPicker {

    // signal in ingresso di sola lettura
    roomCategory = input.required<RoomCategory>();
   
    from    = signal<Date | null>(null);
    to      = signal<Date | null>(null);

    // signal bidirezionale per selezionare la categoria
    selected = model.required<RoomWithCategory | null>();

    selectCategory(category:RoomCategory, name:string){
        let res: RoomWithCategory = {roomCategory:category, roomName:name};
        this.selected.set(res);
    }
    
}
