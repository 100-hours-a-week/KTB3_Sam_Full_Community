package com.example.community.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record UserModifyRequest(
        @NotNull
        String nickname,
        @Nullable
        Long profileImageId
) {
}
