import { Component, effect, inject, input, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { VisitorService } from '../services/visitor-service';
import { Visitor } from '../model/entities';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-guest-picker',
  imports: [FormsModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatIconModule],
  templateUrl: './guest-picker.html',
  styleUrl: './guest-picker.css',
})
export class GuestPicker
{

  private visitorService = inject(VisitorService);

  // INPUT — il padre incrementa questo numero ogni volta che vuole resettare il picker
  // effect() lo osserva e chiama reset() automaticamente quando cambia
  resetTrigger = input<number>(0);

  // OUTPUT — emette il visitatore scelto (trovato o appena creato) verso il componente padre
  // il padre si limita ad ascoltare questo evento senza sapere nulla della logica interna
  visitorSelected = output<Visitor>();

  // STATO INTERNO — non serve esporlo: il padre riceve solo il risultato finale via output
  searchQuery    = signal<string>('');
  searchResults  = signal<Visitor[]>([]);
  showCreateForm = signal<boolean>(false);
  errorMessage   = signal<string>('');

  // oggetto plain per il form di creazione — legato a [(ngModel)]
  newVisitor = { firstName: '', lastName: '', dateOfBirth: '', ssn: '' };

  constructor() {
    // quando resetTrigger cambia valore, il picker si azzera automaticamente
    // effect() è il modo reattivo di reagire ai cambiamenti di un signal in Angular
    effect(() => {
      const _ = this.resetTrigger(); // leggo il signal per registrare la dipendenza
      this.reset();
    });
  }

  // cerca automaticamente ad ogni carattere digitato (da 2 caratteri in su)
  onSearchChange(value: string): void
  {
    this.searchQuery.set(value);
    this.showCreateForm.set(false);
    this.searchResults.set([]);

    const query = value.toLowerCase().trim();
    if (query.length < 2) return; // aspetto almeno 2 caratteri per non fare chiamate inutili

    const request = this.visitorService.findAll();
    request.subscribe({
      next: (visitors) =>
      {
        // filtro client-side per nome o cognome
        const results = visitors.filter(v =>
          v.firstName.toLowerCase().includes(query) ||
          v.lastName.toLowerCase().includes(query)
        );
        this.searchResults.set(results);
        // se non trovo nessuno, apro subito il form di creazione senza richiedere click
        if (results.length === 0)
          this.showCreateForm.set(true);
      },
      error: () => this.errorMessage.set('Errore durante la ricerca.')
    });
  }

  // l'utente ha cliccato un risultato: emetto il visitatore verso il padre e chiudo i risultati
  selectVisitor(visitor: Visitor): void
  {
    this.searchResults.set([]);
    this.showCreateForm.set(false);
    this.visitorSelected.emit(visitor); // comunico al padre chi è stato scelto
  }

  // crea un nuovo visitatore sul backend, poi lo emette verso il padre come se fosse stato trovato
  createVisitor(): void
  {
    const request = this.visitorService.save(this.newVisitor as Visitor);
    request.subscribe({
      next: (created) => {
        this.showCreateForm.set(false);
        this.newVisitor = { firstName: '', lastName: '', dateOfBirth: '', ssn: '' };
        this.visitorSelected.emit(created); // il padre riceve il nuovo visitatore esattamente come uno esistente
      },
      error: () => this.errorMessage.set('Errore durante la creazione del visitatore.')
    });
  }

  // resetta tutto lo stato interno del picker
  reset(): void
  {
    this.searchQuery.set('');
    this.searchResults.set([]);
    this.showCreateForm.set(false);
    this.errorMessage.set('');
    this.newVisitor = { firstName: '', lastName: '', dateOfBirth: '', ssn: '' };
  }
}
