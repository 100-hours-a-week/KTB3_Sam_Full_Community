package com.example.community.repository;

import com.example.community.entity.Like;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class LikeRepository extends BoardLinkedRepository<Like>{
    public Optional<Like> findByUserIdAndBoardId(Long userId, Long boardId) {
        List<Long> likeIds = indexMap.get(boardId);
        return likeIds.stream()
                .map(db::get)
                .filter(Objects::nonNull)
                .filter(like -> like.getUserId().equals(userId))
                .findFirst();
    }
}
