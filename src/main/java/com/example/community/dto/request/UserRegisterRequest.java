package com.example.community.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UserRegisterRequest(
        @Schema(description = "이메일", example = "test@test.co.kr")
        @NotNull
        String email,
        @Schema(description = "비밀번호", example = "dsfh2343@@")
        @NotNull
        String password,
        @Schema(description = "닉네임", example = "닉네임1234")
        @NotNull
        String nickname,
        @Schema(description = "프로필 이미지 ID", example = "3")
        @NotNull
        Long profileImageId
) {
}
