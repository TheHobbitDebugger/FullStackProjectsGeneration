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
    private int             price;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User            seller;

    @ManyToOne
    @JoinColumn(name = "visitor_id")
    private Visitor         visitor;


    private void applyPricePolicy()
    {
        if (visitor != null && visitor.getDateOfBirth() != null)
        {
            int age = Period.between(visitor.getDateOfBirth(), LocalDate.now()).getYears();
            this.price = (age >= 70) ? 5 : 10;
        }
    }
}
