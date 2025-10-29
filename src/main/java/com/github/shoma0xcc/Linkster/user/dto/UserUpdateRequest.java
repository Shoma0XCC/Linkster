package com.github.shoma0xcc.Linkster.user.dto;


import jakarta.validation.constraints.*;

public record UserUpdateRequest(
        @NotBlank @Size(max = 50) String username,
        @NotBlank @Email @Size(max = 254) String email,
        @NotBlank @Size(max = 100) String firstName,
        @NotBlank @Size(max = 100) String lastName,
        @Size(max = 150) String status
) {}
