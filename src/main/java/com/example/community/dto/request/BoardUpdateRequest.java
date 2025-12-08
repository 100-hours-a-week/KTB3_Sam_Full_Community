package com.example.community.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record BoardUpdateRequest(
        @Schema(description = "게시글 제목", example = "게시글 제목 예시2")
        @NotNull
        String title,
        @Schema(description = "게시글 내용", example = "게시글 내용 예시2")
        @NotNull
        String content,
        @Schema(description = "게시글 이미지 ID", example = "[1,2]")
        @NotNull
        List<Long> boardImageIds
) {
}
