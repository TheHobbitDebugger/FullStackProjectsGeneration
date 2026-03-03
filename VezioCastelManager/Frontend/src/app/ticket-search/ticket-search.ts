import { Component, inject, OnInit, signal } from '@angular/core';
import { TicketService } from '../services/ticket-service';
import { Ticket } from '../model/entities';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ConfirmDialog } from './confirm-dialog/confirm-dialog';

@Component({
  selector: 'app-ticket-search',
  imports: [MatTableModule, MatButtonModule, MatIconModule, MatDialogModule],
  templateUrl: './ticket-search.html',
  styleUrl: './ticket-search.css',
})
export class TicketSearch implements OnInit {

  // ticketService gestisce le chiamate HTTP verso /vcm/api/tickets
  private ticketService = inject(TicketService);

  // MatDialog è il servizio Angular Material per aprire finestre modali
  private dialog        = inject(MatDialog);

  // signal che contiene la lista dei biglietti: quando cambia, il template si aggiorna da solo
  tickets      = signal<Ticket[]>([]);
  errorMessage = signal<string>('');

  // nomi delle colonne della tabella — devono corrispondere ai matColumnDef nel template
  columns = ['id', 'date', 'visitor', 'price', 'seller', 'actions'];

  // ngOnInit viene chiamato da Angular appena il componente è pronto:
  // è il posto giusto per caricare i dati iniziali
  ngOnInit(): void {
    this.load();
  }

  // carica tutti i biglietti dal backend e li mette nel signal
  load(): void {
    const request = this.ticketService.findAll();
    request.subscribe({
      next:  (data) => this.tickets.set(data),
      error: ()     => this.errorMessage.set('Errore durante il caricamento dei biglietti.')
    });
  }

  // safe delete: apre il dialog di conferma prima di eliminare
  // così evitiamo cancellazioni accidentali con un semplice click
  safeDelete(id: number): void {
    const ref = this.dialog.open(ConfirmDialog); // apro il modal di conferma

    // afterClosed() è un Observable che emette il valore passato a dialogRef.close()
    // true = l'utente ha confermato, false/undefined = ha annullato
    ref.afterClosed().subscribe((confirmed: boolean) => {
      if (confirmed) {
        const request = this.ticketService.delete(id);
        request.subscribe({
          next:  () => this.load(), // ricarico la lista per riflettere l'eliminazione
          error: () => this.errorMessage.set('Errore durante la cancellazione.')
        });
      }
    });
  }
}
