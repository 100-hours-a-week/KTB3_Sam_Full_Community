package com.example.community.event;

import java.util.List;

public record BoardSavedEvent(
        Long boardId,
        List<Long> boardImageIds
) {
}
