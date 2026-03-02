package com.generation.veziocastelmanager.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int             id;

    private LocalDate       date;

    // il prezzo viene calcolato dal metodo applyPricePolicy() prima di salvare
    private int             price;

    // FK verso User — chi ha venduto il biglietto
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User            seller;

    // FK verso Visitor — a chi è intestato il biglietto
    @ManyToOne
    @JoinColumn(name = "visitor_id")
    private Visitor         visitor;

    // calcola il prezzo in base all'età del visitor
    // over 70 anni: 5€, altrimenti 10€
    // viene chiamato manualmente nel TicketService prima di salvare
    //in alternativa si può aggiungere @PrePersist qui sopra
    //       e JPA lo chiama da solo prima di ogni INSERT, senza doverlo chiamare nel service
    public void applyPricePolicy()
    {
        if (visitor != null && visitor.getDateOfBirth() != null)
        {
            int age = Period.between(visitor.getDateOfBirth(), LocalDate.now()).getYears();
            this.price = (age >= 70) ? 5 : 10;
        }
    }
}
