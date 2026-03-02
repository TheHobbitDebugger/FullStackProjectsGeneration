import { RoomCategory } from "./hotel.entities";

// un wrapper, una room category con room name
export interface RoomWithCategory{
    roomCategory:RoomCategory;
    roomName:string;
}