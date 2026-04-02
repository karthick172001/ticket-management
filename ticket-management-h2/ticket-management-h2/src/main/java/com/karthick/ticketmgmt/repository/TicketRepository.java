package com.karthick.ticketmgmt.repository;

import com.karthick.ticketmgmt.entity.Ticket;
import com.karthick.ticketmgmt.entity.User;
import com.karthick.ticketmgmt.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByCreatedBy(User user);
    List<Ticket> findByAssignedTo(User agent);
    List<Ticket> findByStatus(TicketStatus status);
    List<Ticket> findByCreatedByAndStatus(User user, TicketStatus status);
}
