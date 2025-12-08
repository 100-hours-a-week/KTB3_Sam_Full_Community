package com.example.community.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record PasswordModifyRequest(
        @Schema(description = "변경할 비밀번호", example = "sdhfu2#@")
        @NotNull
        String password,
        @Schema(description = "비밀번호 확인", example = "sdhfu2#@")
        @NotNull
        String checkPassword
) {
}
