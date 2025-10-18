package com.example.community.dto;

import java.util.List;

public record PagedData<T>(
        List<T> content,
        PageInfo pageInfo
) {
}
