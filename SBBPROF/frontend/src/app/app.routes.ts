import { Routes } from '@angular/router';
import { BookRoom } from './book-room/book-room';
import { FormLogin } from './form-login/form-login';
import { authGuard } from './guards/auth.guard';
import { RoomManagement } from './room-management/room-management';

export const routes: Routes = [
    { path:'', component:BookRoom, canActivate: [authGuard]},
    { path:'login', component:FormLogin},
    {path:'rooms', component:RoomManagement, canActivate: [authGuard]}
];
