package com.generation.veziocastelmanager.mapper;

import com.generation.veziocastelmanager.dto.TicketDTO;
import com.generation.veziocastelmanager.model.entities.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, VisitorMapper.class})
public interface TicketMapper
{

    @Mapping(source = "seller.id", target = "sellerId")
    @Mapping(source = "visitor.id", target = "visitorId")
    TicketDTO           toDTO(Ticket ticket);
    List<TicketDTO>     toDTOs(List<Ticket> tickets);

    @Mapping(source = "sellerId", target = "seller.id")
    @Mapping(source = "visitorId", target = "visitor.id")
    Ticket              toEntity(TicketDTO ticketDTO);
    List<Ticket>        toEntities(List<TicketDTO> ticketDTOs);
}
