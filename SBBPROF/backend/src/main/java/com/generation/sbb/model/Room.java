package com.generation.sbb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @NotNull(message = "Floor is required")
    int floor;

    @ManyToOne
    @JoinColumn(name = "room_category_id")
    private RoomCategory category;

}