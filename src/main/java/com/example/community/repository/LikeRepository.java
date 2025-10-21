package com.example.community.repository;

import com.example.community.entity.Like;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class LikeRepository {
    private Map<Long, Like> likeDB;
    private Map<Long, List<Long>> boardIndexMap;
    private long sequence = 0L;

    LikeRepository() {
        this.likeDB = new LinkedHashMap<>();
        this.boardIndexMap = new ConcurrentHashMap<>();
    }

    public Like save(Like like) {
        if(like.getId() == null) {
            like.setId(++sequence);
            if(!boardIndexMap.containsKey(like.getBoardId())) {
                List<Long> likeIds = new ArrayList<>();
                likeIds.add(like.getId());
                boardIndexMap.put(like.getBoardId(), likeIds);
            } else {
                boardIndexMap.get(like.getBoardId()).add(like.getId());
            }
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

    public Optional<Like> findByUserIdAndBoardId(Long userId, Long boardId) {
        List<Long> likeIds = boardIndexMap.get(boardId);
        return likeIds.stream()
                .map(likeDB::get)
                .filter(Objects::nonNull)
                .filter(like -> like.getUserId().equals(userId))
                .findFirst();
    }

    public void deleteById(Long id) {
        Like like = likeDB.remove(id);
        boardIndexMap.get(like.getBoardId()).remove(id);
    }

    public void deleteByBoardIdWithIndex(Long boardId) {
        List<Long> deleteLikeIds = boardIndexMap.remove(boardId);
        deleteLikeIds.forEach(likeDB::remove);
    }

    public void deleteByBoardIdWithoutIndex(Long boardId) {
        likeDB.values().removeIf(like -> like.getBoardId().equals(boardId));
    }

    public List<Like> findAllByBoardId(Long boardId) {
        List<Long> foundLikeIds = boardIndexMap.get(boardId);
        return foundLikeIds.stream()
                .map(likeDB::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<Like> findAllByBoardIds(List<Long> boardIds) {
        return boardIds.stream()
                .flatMap(boardId -> {
                    List<Long> ids = boardIndexMap.get(boardId);
                    return ids.stream()
                            .map(likeDB::get)
                            .filter(Objects::nonNull);
                })
                .collect(Collectors.toList());
    }
}
