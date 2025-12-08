package com.example.community.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record ReissueRequest(
        @Schema(description = "리프레시 토큰", example = "sdfiho234849@Gd")
        String refreshToken
) {
}
