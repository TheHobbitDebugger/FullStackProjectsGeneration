package com.generation.sbb.mapper;

import com.generation.sbb.dto.RoomDTO;
import com.generation.sbb.model.Room;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    RoomDTO toDTO(Room room);
    List<RoomDTO> toDTOs(List<Room> rooms);

    Room toEntity(RoomDTO roomDTO);
    List<Room> toEntities(List<RoomDTO> roomDTOs);
}