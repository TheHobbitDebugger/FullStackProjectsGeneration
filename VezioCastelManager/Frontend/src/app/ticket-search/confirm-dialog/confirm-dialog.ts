import { Component, inject } from '@angular/core';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-confirm-dialog',
  imports: [MatDialogModule, MatButtonModule],
  template: `
    <h2 mat-dialog-title>Conferma eliminazione</h2>
    <mat-dialog-content>Sei sicuro di voler eliminare questo biglietto?</mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button       (click)="close(false)">Annulla</button>
      <button mat-flat-button  (click)="close(true)">Elimina</button>
    </mat-dialog-actions>
  `,
})
export class ConfirmDialog {
  private dialogRef = inject(MatDialogRef<ConfirmDialog>);

  // chiude il dialog e passa il risultato al chiamante
  close(confirmed: boolean): void {
    this.dialogRef.close(confirmed);
  }
}
