package com.example.community.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record CommentPostRequest(
        @Schema(description = "댓글 내용", example = "댓글 예시1")
        @NotNull
        String content
) {
}
