package com.example.community.repository.inmemory;

import com.example.community.entity.Like;
import lombok.Locked;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryLikeRepository extends InMemoryBoardUserLinkedRepository<Like> {
    @Locked.Read
    public Optional<Like> findByUserIdAndBoardId(Long userId, Long boardId) {
        return Optional.ofNullable(indexMap.get(boardId).get(userId)).map(db::get);
    }
}
