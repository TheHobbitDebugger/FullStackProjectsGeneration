package com.generation.sbb.mapper;

import com.generation.sbb.dto.RoomCategoryDTO;
import com.generation.sbb.model.RoomCategory;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomCategoryMapper {

    RoomCategoryDTO toDTO(RoomCategory roomCategory);
    List<RoomCategoryDTO> toDTOs(List<RoomCategory> roomCategories);
    RoomCategory toEntity(RoomCategoryDTO roomCategoryDTO);
    List<RoomCategory> toEntities(List<RoomCategoryDTO> roomCategoryDTOs);  


}
