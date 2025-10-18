package com.example.community.dto;

import java.util.List;

public record PageInfo(
        int pageNumber,
        int pageSize,
        boolean first,
        boolean last,
        int totalElements,
        int totalPages,
        boolean empty
) {
    public static <T> PageInfo from(List<T> data, int totalElements, int page, int size) {
        int totalPages = (int) Math.ceil((double) totalElements / size);
        boolean first = page == 1;
        boolean last = page == totalPages;
        boolean empty = data.isEmpty();

        return new PageInfo(page,size,first,last,totalElements,totalPages,empty);

    }
}
