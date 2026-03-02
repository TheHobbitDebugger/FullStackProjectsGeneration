package com.generation.sbb.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoomCategoryDTO {

    private int id;

    @NotNull(message = "Name is required")
    private String name;
 
    @NotNull(message = "Description is required")
    private String description;

    private String[] images;

    int price;

    // UNA SCICCHERIA
    // preparo un attributo che NON DERIVA dall'entità
    // ma dal metodo del repository
    // un attributo calcolato 
    // che mi dice QUANTE 
    // di queste stanze sono libere in QUESTO HOTEL
    int free;

    // stanze
    List<String> roomNames = new ArrayList<String>();

    // stanza di base
    String defaultRoom;

}
