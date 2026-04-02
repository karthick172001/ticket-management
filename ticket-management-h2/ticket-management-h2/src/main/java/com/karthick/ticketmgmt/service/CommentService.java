package com.karthick.ticketmgmt.service;

import com.karthick.ticketmgmt.dto.AppDTOs.*;
import com.karthick.ticketmgmt.entity.Comment;
import com.karthick.ticketmgmt.entity.Ticket;
import com.karthick.ticketmgmt.entity.User;
import com.karthick.ticketmgmt.enums.Role;
import com.karthick.ticketmgmt.exception.ResourceNotFoundException;
import com.karthick.ticketmgmt.repository.CommentRepository;
import com.karthick.ticketmgmt.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;

    public CommentResponse addComment(Long ticketId, CommentRequest request, User currentUser) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + ticketId));

        // Users can only comment on their own tickets
        if (currentUser.getRole() == Role.USER &&
                !ticket.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only comment on your own tickets");
        }

        Comment comment = Comment.builder()
                .content(request.getContent())
                .ticket(ticket)
                .commentedBy(currentUser)
                .build();

        return mapToResponse(commentRepository.save(comment));
    }

    public List<CommentResponse> getCommentsByTicket(Long ticketId, User currentUser) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + ticketId));

        if (currentUser.getRole() == Role.USER &&
                !ticket.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only view comments on your own tickets");
        }

        return commentRepository.findByTicket(ticket).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private CommentResponse mapToResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .commentedBy(comment.getCommentedBy().getName())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
