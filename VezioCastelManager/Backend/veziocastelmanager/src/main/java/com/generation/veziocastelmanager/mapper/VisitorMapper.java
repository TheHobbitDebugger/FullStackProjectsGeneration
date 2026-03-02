package com.generation.veziocastelmanager.mapper;

import com.generation.veziocastelmanager.dto.VisitorDTO;
import com.generation.veziocastelmanager.model.entities.Visitor;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VisitorMapper
{
    VisitorDTO          toDTO(Visitor visitor);
    List<VisitorDTO>    toDTOs(List<Visitor> visitors);
    Visitor             toEntity(VisitorDTO visitorDTO);
    List<Visitor>       toEntities(List<VisitorDTO> visitorDTOs);
}
