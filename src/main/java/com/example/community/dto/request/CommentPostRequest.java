package com.example.community.dto.request;

import jakarta.validation.constraints.NotNull;

public record CommentPostRequest(
        @NotNull
        String content
) {
}
