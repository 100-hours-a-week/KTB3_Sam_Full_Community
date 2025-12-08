package com.example.community.dto.response;

import com.example.community.common.SuccessCode;

public record APIResponse<T>(
        String message,
        T data
) {
    public static <T> APIResponse<T> success(SuccessCode successCode, T data) {
        return new APIResponse<>(successCode.getMessage(), data);
    }
}
