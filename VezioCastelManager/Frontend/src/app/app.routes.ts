import { Routes } from '@angular/router';
import { LoginForm } from './login-form/login-form';
import { TicketForm } from './ticket-form/ticket-form';
import { TicketSearch } from './ticket-search/ticket-search';
import { VisitorForm } from './visitor-form/visitor-form';
import { authGuard } from './guard/auth-guard';

export const routes: Routes = [
  { path: 'login',          component: LoginForm   },
  { path: 'visitor-form',   component: VisitorForm, canActivate: [authGuard] },
  { path: 'ticket-form',    component: TicketForm,  canActivate: [authGuard] },
  { path: 'ticket-search',  component: TicketSearch,canActivate: [authGuard] },
  { path: '',               redirectTo: 'ticket-form', pathMatch: 'full' },
  { path: '**',             redirectTo: 'ticket-form' },
];
