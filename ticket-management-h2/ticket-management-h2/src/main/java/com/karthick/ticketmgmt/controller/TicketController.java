package com.karthick.ticketmgmt.controller;

import com.karthick.ticketmgmt.dto.AppDTOs.*;
import com.karthick.ticketmgmt.entity.User;
import com.karthick.ticketmgmt.enums.TicketStatus;
import com.karthick.ticketmgmt.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    // Create a new ticket (USER, AGENT, ADMIN)
    @PostMapping
    public ResponseEntity<ApiResponse<TicketResponse>> createTicket(
            @Valid @RequestBody TicketCreateRequest request,
            @AuthenticationPrincipal User currentUser) {
        TicketResponse ticket = ticketService.createTicket(request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Ticket created successfully", ticket));
    }

    // Get all tickets (role-based filtering)
    @GetMapping
    public ResponseEntity<ApiResponse<List<TicketResponse>>> getAllTickets(
            @AuthenticationPrincipal User currentUser) {
        List<TicketResponse> tickets = ticketService.getAllTickets(currentUser);
        return ResponseEntity.ok(ApiResponse.success("Tickets fetched successfully", tickets));
    }

    // Get ticket by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TicketResponse>> getTicketById(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        TicketResponse ticket = ticketService.getTicketById(id, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Ticket fetched successfully", ticket));
    }

    // Update ticket
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TicketResponse>> updateTicket(
            @PathVariable Long id,
            @RequestBody TicketUpdateRequest request,
            @AuthenticationPrincipal User currentUser) {
        TicketResponse ticket = ticketService.updateTicket(id, request, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Ticket updated successfully", ticket));
    }

    // Assign ticket to agent (ADMIN only)
    @PatchMapping("/{id}/assign")
    public ResponseEntity<ApiResponse<TicketResponse>> assignTicket(
            @PathVariable Long id,
            @Valid @RequestBody AssignTicketRequest request,
            @AuthenticationPrincipal User currentUser) {
        TicketResponse ticket = ticketService.assignTicket(id, request, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Ticket assigned successfully", ticket));
    }

    // Resolve ticket (AGENT, ADMIN)
    @PatchMapping("/{id}/resolve")
    public ResponseEntity<ApiResponse<TicketResponse>> resolveTicket(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        TicketResponse ticket = ticketService.resolveTicket(id, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Ticket resolved successfully", ticket));
    }

    // Close ticket (ADMIN only)
    @PatchMapping("/{id}/close")
    public ResponseEntity<ApiResponse<TicketResponse>> closeTicket(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        TicketResponse ticket = ticketService.closeTicket(id, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Ticket closed successfully", ticket));
    }

    // Filter by status (ADMIN only)
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<TicketResponse>>> getByStatus(
            @PathVariable TicketStatus status,
            @AuthenticationPrincipal User currentUser) {
        List<TicketResponse> tickets = ticketService.getTicketsByStatus(status, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Tickets fetched by status", tickets));
    }
}
