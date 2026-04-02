package com.karthick.ticketmgmt.dto;

import com.karthick.ticketmgmt.enums.Role;
import com.karthick.ticketmgmt.enums.TicketPriority;
import com.karthick.ticketmgmt.enums.TicketStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class AppDTOs {

    // ===== AUTH =====
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequest {
        @Email @NotBlank
        private String email;
        @NotBlank
        private String password;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthResponse {
        private String token;
        private String name;
        private String email;
        private Role role;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignupRequest {
        @NotBlank(message = "Name is required")
        private String name;
        @Email @NotBlank
        private String email;
        @NotBlank
        private String password;
        @NotNull
        private Role role;
    }

    // ===== TICKET =====
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TicketCreateRequest {
        @NotBlank(message = "Title is required")
        private String title;
        @NotBlank(message = "Description is required")
        private String description;
        @NotNull(message = "Priority is required")
        private TicketPriority priority;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TicketUpdateRequest {
        private String title;
        private String description;
        private TicketPriority priority;
        private TicketStatus status;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssignTicketRequest {
        @NotNull(message = "Agent ID is required")
        private Long agentId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TicketResponse {
        private Long id;
        private String title;
        private String description;
        private TicketStatus status;
        private TicketPriority priority;
        private String createdBy;
        private String assignedTo;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    // ===== COMMENT =====
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentRequest {
        @NotBlank(message = "Comment content is required")
        private String content;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentResponse {
        private Long id;
        private String content;
        private String commentedBy;
        private LocalDateTime createdAt;
    }

    // ===== API RESPONSE WRAPPER =====
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiResponse<T> {
        private boolean success;
        private String message;
        private T data;

        public static <T> ApiResponse<T> success(String message, T data) {
            return ApiResponse.<T>builder()
                    .success(true)
                    .message(message)
                    .data(data)
                    .build();
        }

        public static <T> ApiResponse<T> error(String message) {
            return ApiResponse.<T>builder()
                    .success(false)
                    .message(message)
                    .build();
        }
    }
}
