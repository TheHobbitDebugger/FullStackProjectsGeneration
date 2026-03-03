package com.generation.veziocastelmanager.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Visitor
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int             id;
    private String          firstName;
    private String          lastName;
    private LocalDate       dateOfBirth;
    private String          ssn;

    @OneToMany(mappedBy = "visitor")
    private List<Ticket>    tickets;
}
