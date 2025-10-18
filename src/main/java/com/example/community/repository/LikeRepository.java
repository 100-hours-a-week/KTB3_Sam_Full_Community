package com.example.community.repository;

import com.example.community.entity.Comment;
import com.example.community.entity.Like;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class LikeRepository {
    private Map<Long, Like> likeDB;
    private long sequence = 0L;

    LikeRepository() {
        this.likeDB = new LinkedHashMap<>();
    }

    public Like save(Like like) {
        if(like.getId() == null) {
            like.setId(++sequence);
        }
        likeDB.put(like.getId(), like);
        return like;
    }

    public List<Like> findAll() {
        return new ArrayList<>(likeDB.values());
    }

    public Optional<Like> findById(Long id) {
        return Optional.ofNullable(likeDB.get(id));
    }

    public void deleteById(Long id) {
        likeDB.remove(id);
    }

    public List<Like> findAllByBoardId(Long boardId) {
        return likeDB.values().stream()
                .filter(like -> like.getBoardId().equals(boardId))
                .toList();
    }

    public List<Like> findAllByBoardIds(List<Long> boardIds) {
        return likeDB.values().stream()
                .filter(like -> boardIds.contains(like.getBoardId()))
                .toList();
    }
}
