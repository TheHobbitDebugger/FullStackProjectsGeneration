package com.generation.sbb.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomDTO {

    private int id;

    @NotNull(message = "Floor is required")
    int floor;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Hotel ID is required")
    private Long hotelId;

    private RoomCategoryDTO category;


}