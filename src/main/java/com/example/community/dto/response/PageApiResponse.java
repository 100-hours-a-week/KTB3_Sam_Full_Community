package com.example.community.dto.response;

import com.example.community.common.SuccessCode;
import com.example.community.dto.PageInfo;
import com.example.community.dto.PagedData;

import java.util.List;

public record PageApiResponse<T>(
        String message,
        List<T> data,
        PageInfo pageInfo
) {
    public static <T> PageApiResponse<T> success(SuccessCode successCode, PagedData pagedData) {
        return new PageApiResponse<>(successCode.getMessage(), pagedData.content(), pagedData.pageInfo());
    }
}
