export interface Guest {
    id?:number;
    firstName:string;
    lastName:string;
    ssn:string;
    dob?:Date;
    address:string;
    city:string;
}

export interface Room {
    id?:number;
    name:string;
    hotelId:number;
    roomCategory?:RoomCategory;
}

// RoomCategoryDTO
export interface RoomCategory {
    id?:number;
    name:string;
    description:string;
    price:number;
    images:string[],
    free?:number; // numero di stanze libere in un dato momento, mi arriva dal backend
    roomNames:string[];
    defaultRoom:string;
}

export interface Booking{
    id?:number;
    guestId:number;
    roomId:number;
    guest:Guest;
    room:Room;
    from:Date;
    to:Date;
    notes:string;
    status:string;
    price:number;
}

export interface Hotel{
    id:number;
    name:string;
    address:string;
    city:string;
}

export interface User{
    id:number;
    email:string;
    firstName:string;
    lastName:string;
    role:string;
    hotel:Hotel;
}