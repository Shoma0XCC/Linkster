package com.github.shoma0xcc.Linkster.user.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;

public record UserPatchRequest(
        @Nullable @Size(max = 50) String username,
        @Nullable @Email @Size(max = 254) String email,
        @Nullable @Size(max = 100) String firstName,
        @Nullable @Size(max = 100) String lastName,
        @Nullable @Size(max = 150) String status
) {}
