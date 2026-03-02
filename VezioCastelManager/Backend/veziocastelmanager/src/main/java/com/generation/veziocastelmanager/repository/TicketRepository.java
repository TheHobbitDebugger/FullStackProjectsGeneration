package com.generation.veziocastelmanager.repository;

import com.generation.veziocastelmanager.model.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer>
{
    List<Ticket>        findBySellerId(int sellerId     );
    List<Ticket>        findByVisitorId(int visitorId   );
    List<Ticket>        findByDate(LocalDate date       );
}
