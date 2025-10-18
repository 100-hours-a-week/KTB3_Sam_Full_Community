package com.example.community.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record BoardUpdateRequest(
        @NotNull
        String title,
        @NotNull
        String content,
        @NotNull
        List<Long> boardImageIds
) {
}
