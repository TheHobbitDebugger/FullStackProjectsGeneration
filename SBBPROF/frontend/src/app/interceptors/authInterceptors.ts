import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

// Ciao mamma, io vengo eseguito in maniera trasparente a ogni REQUEST
// e modifico le request
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const token = localStorage.getItem('token'); // Recupera il JWT

  let authReq = req;

  // --- 1. AGGIUNTA DEL TOKEN (LOGICA IN USCITA) ---
  if (token) {
    // Cloniamo la richiesta originale per iniettare l'header Authorization
    authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  // --- 2. GESTIONE ERRORE (LOGICA IN ENTRATA) ---
  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      
      // Se il server risponde 401 (Non autorizzato) o 403 (Proibito)
      if (error.status === 401 || error.status === 403) {
        console.error('Sessione scaduta o non autorizzata. Reindirizzamento...');
        
        // Puliamo il localStorage perché il token non è più valido
        localStorage.removeItem('token');
        
        // Portiamo l'utente alla pagina di login
        router.navigate(['/login']);
      }

      // Propaghiamo l'errore al componente che ha fatto la chiamata
      return throwError(() => error);
    })
  );
};