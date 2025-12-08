package com.example.community.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record UserModifyRequest(
        @Schema(description = "닉네임", example = "닉네임1234")
        @NotNull
        String nickname,
        @Schema(description = "프로필 이미지 ID", example = "2")
        @Nullable
        Long profileImageId
) {
}
