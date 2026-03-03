import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TicketService } from '../services/ticket-service';
import { UserService } from '../services/user-service';
import { Visitor } from '../model/entities';
import { GuestPicker } from '../guest-picker/guest-picker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

/**
 * Form per la vendita di un nuovo biglietto.
 * La ricerca/creazione del visitatore è delegata a GuestPicker:
 * questo componente si occupa solo di ricevere il visitatore scelto
 * e di inviare il biglietto al backend.
 */
@Component({
  selector: 'app-ticket-form',
  imports: [FormsModule, GuestPicker, MatFormFieldModule, MatInputModule, MatButtonModule, MatIconModule],
  templateUrl: './ticket-form.html',
  styleUrl: './ticket-form.css',
})
export class TicketForm {

  private ticketService = inject(TicketService);
  private userService   = inject(UserService);

  readonly today = new Date().toISOString().split('T')[0];

  // visitatore ricevuto da GuestPicker tramite l'evento (visitorSelected)
  selectedVisitor = signal<Visitor | null>(null);

  // data del biglietto — modificabile dall'utente, default oggi
  date = signal<string>(this.today);

  // incrementato dopo ogni submit riuscito per notificare GuestPicker di resettarsi
  // GuestPicker osserva questo valore con effect() e si azzera automaticamente
  resetTrigger = signal<number>(0);

  success      = signal<boolean>(false);
  errorMessage = signal<string>('');

  // true solo se un visitatore è stato selezionato — usato per disabilitare campi e pulsante
  visitorSelected = computed(() => this.selectedVisitor() !== null);

  // chiamato da GuestPicker tramite (visitorSelected): ricevo il visitatore e lo salvo nel signal
  onVisitorSelected(visitor: Visitor): void {
    this.selectedVisitor.set(visitor);
  }

  // deseleziona il visitatore e incrementa resetTrigger per azzerare GuestPicker
  resetVisitor(): void {
    this.selectedVisitor.set(null);
    this.resetTrigger.update(v => v + 1);
  }

  // vende il biglietto: manda visitatore, data e venditore al backend
  // il prezzo non viene mandato perché lo calcola il backend in base all'età
  submit(): void {
    const seller = this.userService.loggedUser();
    if (!seller || !this.selectedVisitor()) return;

    const request = this.ticketService.save({
      id:        0,          // il backend assegna l'id
      date:      this.date(),
      price:     0,          // calcolato dal backend
      sellerId:  seller.id,
      visitorId: this.selectedVisitor()!.id,
    });

    request.subscribe({
      next: () => {
        this.success.set(true);
        this.errorMessage.set('');
        this.resetForm();
      },
      error: err => {
        const msg = err.error?.message ?? 'Errore durante il salvataggio.';
        this.errorMessage.set(msg);
      }
    });
  }

  private resetForm(): void {
    this.selectedVisitor.set(null);
    this.date.set(this.today);
    this.resetTrigger.update(v => v + 1); // segnala a GuestPicker di resettarsi
  }
}
