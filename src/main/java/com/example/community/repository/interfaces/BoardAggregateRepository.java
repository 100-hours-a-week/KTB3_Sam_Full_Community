package com.example.community.repository.interfaces;

import com.example.community.entity.interfaces.BoardLinked;

import java.util.List;

public interface BoardAggregateRepository<T extends BoardLinked> {
    public void deleteByBoardId(Long boardId);

    public List<T> findAllByBoardId(Long boardId);

    public List<T> findAllByBoardIds(List<Long> boardIds);

    public List<T> findPageByBoardId(Long board, int page, int size);
}
