package com.karthick.ticketmgmt.controller;

import com.karthick.ticketmgmt.dto.AppDTOs.*;
import com.karthick.ticketmgmt.entity.User;
import com.karthick.ticketmgmt.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets/{ticketId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> addComment(
            @PathVariable Long ticketId,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal User currentUser) {
        CommentResponse comment = commentService.addComment(ticketId, request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Comment added successfully", comment));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getComments(
            @PathVariable Long ticketId,
            @AuthenticationPrincipal User currentUser) {
        List<CommentResponse> comments = commentService.getCommentsByTicket(ticketId, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Comments fetched successfully", comments));
    }
}
