package com.example.community.dto.response;

public record DuplicationCheckResponse(
        Boolean isDuplicated
) {
    public static DuplicationCheckResponse from(Boolean isPropertyExists) {
        return new DuplicationCheckResponse(isPropertyExists);
    }
}
