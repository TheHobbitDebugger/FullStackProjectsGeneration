import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { VisitorService } from '../services/visitor-service';
import { Visitor } from '../model/entities';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-visitor-form',
  imports: [FormsModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatIconModule],
  templateUrl: './visitor-form.html',
  styleUrl: './visitor-form.css',
})
export class VisitorForm {

  // inietto il service che gestisce le chiamate HTTP verso /vcm/api/visitors
  private visitorService = inject(VisitorService);

  // uso un oggetto plain (non Signal) perché è legato a [(ngModel)]:
  // ngModel usa il two-way binding classico, non i Signal
  form = { firstName: '', lastName: '', dateOfBirth: '', ssn: '' };

  // signal per il feedback: true dopo un salvataggio riuscito
  success      = signal<boolean>(false);
  errorMessage = signal<string>('');

  save(): void {
    // cast a Visitor necessario perché form non ha l'id (viene assegnato dal backend)
    const request = this.visitorService.save(this.form as Visitor);
    request.subscribe({
      next: () => {
        this.success.set(true);    // mostro il messaggio di successo
        this.errorMessage.set('');
        this.reset();              // svuoto il form per permettere un nuovo inserimento
      },
      error: () => this.errorMessage.set('Errore durante il salvataggio.')
    });
  }

  // svuoto tutti i campi riassegnando un oggetto fresco
  reset(): void {
    this.form = { firstName: '', lastName: '', dateOfBirth: '', ssn: '' };
  }
}
