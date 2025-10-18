package com.example.community.dto.response;

import com.example.community.common.SuccessCode;

public record ApiResponse<T>(
        String message,
        T data
) {
    public static <T> ApiResponse<T> success(SuccessCode successCode, T data) {
        return new ApiResponse<>(successCode.getMessage(), data);
    }
}
