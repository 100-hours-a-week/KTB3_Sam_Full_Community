package com.example.community.dto.response;

public record ImageUrlResponse(
        Long imageId,
        String imagePresignedUrl
) {
}
