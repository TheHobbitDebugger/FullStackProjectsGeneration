package com.generation.veziocastelmanager.model.repository;

import com.generation.veziocastelmanager.model.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer>
{

}
