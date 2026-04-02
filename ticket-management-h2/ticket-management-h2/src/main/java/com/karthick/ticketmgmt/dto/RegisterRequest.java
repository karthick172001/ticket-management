package com.karthick.ticketmgmt.dto;

import com.karthick.ticketmgmt.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

// ===== AUTH DTOs =====

@Data
class RegisterRequest {
    @NotBlank(message = "Name is required")
    public String name;

    @Email(message = "Valid email is required")
    @NotBlank(message = "Email is required")
    public String email;

    @NotBlank(message = "Password is required")
    public String password;

    @NotNull(message = "Role is required")
    public Role role;
}
