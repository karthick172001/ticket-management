package com.karthick.ticketmgmt.repository;

import com.karthick.ticketmgmt.entity.Comment;
import com.karthick.ticketmgmt.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTicket(Ticket ticket);
}
