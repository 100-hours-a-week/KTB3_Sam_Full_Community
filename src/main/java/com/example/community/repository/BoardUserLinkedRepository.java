package com.example.community.repository;

import com.example.community.entity.BaseEntity;
import com.example.community.entity.interfaces.BoardLinked;
import com.example.community.entity.interfaces.UserLinked;
import com.example.community.repository.interfaces.BoardAggregateRepository;
import com.example.community.repository.interfaces.CRUDRepository;
import lombok.Locked;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class BoardUserLinkedRepository<T extends BaseEntity & BoardLinked & UserLinked> implements CRUDRepository<T>, BoardAggregateRepository<T> {
    protected Map<Long, T> db = new LinkedHashMap<>();
    protected long sequence = 0L;
    protected Map<Long, Map<Long, Long>> indexMap = new ConcurrentHashMap<>();

    @Locked.Write
    public T save(T entity) {
        if(entity.getId() == null) {
            entity.setId(++sequence);
        }
        db.put(entity.getId(), entity);

        Map<Long, Long> userEntityMap = indexMap.computeIfAbsent(entity.getBoardId(), key -> new ConcurrentHashMap<>());
        userEntityMap.put(entity.getUserId(), entity.getId());

        return entity;
    }

    @Locked.Read
    public Optional<T> findById(Long id) {
        return Optional.ofNullable(db.get(id));
    }

    @Locked.Write
    public void deleteById(Long id) {
        T entity = db.remove(id);

        indexMap.computeIfPresent(entity.getBoardId(), (boardId, userEntityMap) -> {
            userEntityMap.remove(entity.getUserId());
            return userEntityMap;
        });
    }

    @Locked.Write
    public void deleteByBoardId(Long boardId) {
        Map<Long, Long> deleteMap = indexMap.get(boardId);
        deleteMap.values().forEach(db::remove);
    }

    @Locked.Read
    public List<T> findAllByBoardId(Long boardId) {
        Map<Long, Long> foundMap = indexMap.get(boardId);
        return foundMap.values().stream().map(db::get).filter(Objects::nonNull).toList();
    }

    @Locked.Read
    public List<T> findAllByBoardIds(List<Long> boardIds) {
        return boardIds.stream().flatMap(boardId -> {
            Map<Long, Long> userLikeMap = indexMap.getOrDefault(boardId, Map.of());
            return userLikeMap.values().stream().map(db::get).filter(Objects::nonNull);
        }).toList();
    }

    @Locked.Read
    public List<T> findPageByBoardId(Long boardId, int page, int size) {
        List<Long> ids = indexMap.get(boardId).values().stream().filter(Objects::nonNull).toList();

        return ids.stream()
                .skip((long) (page-1) * size)
                .limit(size)
                .map(db::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Locked.Read
    public int count() {
        return db.size();
    }
}
