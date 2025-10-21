package com.example.community.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @Schema(description = "이메일", example = "test@test.co.kr")
        @NotNull
        String email,
        @Schema(description = "비밀번호", example = "sdifod2349i3r")
        @NotNull
        String password
) {

}
