package com.generation.sbb.model;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Name is required")
    private String name;
 
    @NotNull(message = "Description is required")
    private String description;


    @JdbcTypeCode(SqlTypes.JSON)
    private String[] images;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    int price;

}