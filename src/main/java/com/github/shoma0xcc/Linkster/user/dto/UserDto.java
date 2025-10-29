package com.github.shoma0xcc.Linkster.user.dto;

public record UserDto(
        Long id,
        String username,
        String email,
        String firstName,
        String lastName,
        String status
) {}
