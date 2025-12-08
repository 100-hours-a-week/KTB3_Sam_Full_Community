package com.example.community.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record CommentModifyRequest(
        @Schema(description = "수정할 댓글 내용", example = "수정 댓글 내용1")
        @NotNull
        String content
) {
}
