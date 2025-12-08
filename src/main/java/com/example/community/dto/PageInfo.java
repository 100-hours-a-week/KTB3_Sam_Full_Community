package com.example.community.dto;

import org.springframework.data.domain.Page;

public record PageInfo(
        int pageNumber,
        int pageSize,
        boolean first,
        boolean last,
        int totalElements,
        int totalPages,
        boolean empty
) {
    public static <T> PageInfo from(Page<T> data) {
        return new PageInfo(
                data.getPageable().getPageNumber(),
                data.getPageable().getPageSize(),
                data.isFirst(),
                data.isLast(),
                Long.valueOf(data.getTotalElements()).intValue(),
                data.getTotalPages(),
                data.isEmpty()
        );
    }
}
