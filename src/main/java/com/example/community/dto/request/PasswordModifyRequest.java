package com.example.community.dto.request;

import jakarta.validation.constraints.NotNull;

public record PasswordModifyRequest(
        @NotNull
        String password,
        @NotNull
        String checkPassword
) {
}
