package com.karthick.ticketmgmt.service;

import com.karthick.ticketmgmt.dto.AppDTOs.*;
import com.karthick.ticketmgmt.entity.Ticket;
import com.karthick.ticketmgmt.entity.User;
import com.karthick.ticketmgmt.enums.Role;
import com.karthick.ticketmgmt.enums.TicketStatus;
import com.karthick.ticketmgmt.exception.BadRequestException;
import com.karthick.ticketmgmt.exception.ResourceNotFoundException;
import com.karthick.ticketmgmt.repository.TicketRepository;
import com.karthick.ticketmgmt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketResponse createTicket(TicketCreateRequest request, User currentUser) {
        Ticket ticket = Ticket.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .status(TicketStatus.OPEN)
                .createdBy(currentUser)
                .build();

        return mapToResponse(ticketRepository.save(ticket));
    }

    public List<TicketResponse> getAllTickets(User currentUser) {
        List<Ticket> tickets;

        if (currentUser.getRole() == Role.ADMIN) {
            tickets = ticketRepository.findAll();
        } else if (currentUser.getRole() == Role.AGENT) {
            tickets = ticketRepository.findByAssignedTo(currentUser);
        } else {
            tickets = ticketRepository.findByCreatedBy(currentUser);
        }

        return tickets.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public TicketResponse getTicketById(Long id, User currentUser) {
        Ticket ticket = findTicketById(id);
        validateAccess(ticket, currentUser);
        return mapToResponse(ticket);
    }

    public TicketResponse updateTicket(Long id, TicketUpdateRequest request, User currentUser) {
        Ticket ticket = findTicketById(id);

        if (currentUser.getRole() == Role.USER && !ticket.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only update your own tickets");
        }

        if (request.getTitle() != null) ticket.setTitle(request.getTitle());
        if (request.getDescription() != null) ticket.setDescription(request.getDescription());
        if (request.getPriority() != null) ticket.setPriority(request.getPriority());
        if (request.getStatus() != null) {
            if (currentUser.getRole() == Role.USER) {
                throw new AccessDeniedException("Users cannot change ticket status");
            }
            ticket.setStatus(request.getStatus());
        }

        return mapToResponse(ticketRepository.save(ticket));
    }

    public TicketResponse assignTicket(Long ticketId, AssignTicketRequest request, User currentUser) {
        if (currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Only admins can assign tickets");
        }

        Ticket ticket = findTicketById(ticketId);

        User agent = userRepository.findById(request.getAgentId())
                .orElseThrow(() -> new ResourceNotFoundException("Agent not found with id: " + request.getAgentId()));

        if (agent.getRole() != Role.AGENT) {
            throw new BadRequestException("User is not an agent");
        }

        ticket.setAssignedTo(agent);
        ticket.setStatus(TicketStatus.IN_PROGRESS);

        return mapToResponse(ticketRepository.save(ticket));
    }

    public TicketResponse resolveTicket(Long id, User currentUser) {
        Ticket ticket = findTicketById(id);

        if (currentUser.getRole() == Role.USER) {
            throw new AccessDeniedException("Only agents or admins can resolve tickets");
        }

        ticket.setStatus(TicketStatus.RESOLVED);
        return mapToResponse(ticketRepository.save(ticket));
    }

    public TicketResponse closeTicket(Long id, User currentUser) {
        Ticket ticket = findTicketById(id);

        if (currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Only admins can close tickets");
        }

        ticket.setStatus(TicketStatus.CLOSED);
        return mapToResponse(ticketRepository.save(ticket));
    }

    public List<TicketResponse> getTicketsByStatus(TicketStatus status, User currentUser) {
        if (currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Only admins can filter tickets by status");
        }
        return ticketRepository.findByStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private Ticket findTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));
    }

    private void validateAccess(Ticket ticket, User currentUser) {
        if (currentUser.getRole() == Role.ADMIN) return;
        if (currentUser.getRole() == Role.AGENT &&
                ticket.getAssignedTo() != null &&
                ticket.getAssignedTo().getId().equals(currentUser.getId())) return;
        if (currentUser.getRole() == Role.USER &&
                ticket.getCreatedBy().getId().equals(currentUser.getId())) return;
        throw new AccessDeniedException("You don't have access to this ticket");
    }

    private TicketResponse mapToResponse(Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .status(ticket.getStatus())
                .priority(ticket.getPriority())
                .createdBy(ticket.getCreatedBy().getName())
                .assignedTo(ticket.getAssignedTo() != null ? ticket.getAssignedTo().getName() : null)
                .createdAt(ticket.getCreatedAt())
                .updatedAt(ticket.getUpdatedAt())
                .build();
    }
}
