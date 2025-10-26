package com.example.community.repository;

import com.example.community.entity.BaseEntity;
import com.example.community.entity.BoardLinked;
import com.example.community.repository.interfaces.BoardAggregateRepository;
import com.example.community.repository.interfaces.CRUDRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class BoardLinkedRepository<T extends BaseEntity & BoardLinked> implements CRUDRepository<T>, BoardAggregateRepository<T> {
    protected Map<Long, T> db = new LinkedHashMap<>();
    protected long sequence = 0L;
    protected Map<Long, List<Long>> indexMap = new ConcurrentHashMap<>();

    public T save(T entity) {
        if(entity.getId() == null) {
            entity.setId(++sequence);
            if(!indexMap.containsKey(entity.getBoardId())) {
                List<Long> ids = new ArrayList<>();
                ids.add(entity.getId());
                indexMap.put(entity.getBoardId(), ids);
            } else {
                indexMap.get(entity.getBoardId()).add(entity.getId());
            }
        }
        db.put(entity.getId(), entity);
        return entity;
    }

    public Optional<T> findById(Long id) {
        return Optional.ofNullable(db.get(id));
    }

    public void deleteById(Long id) {
        T entity = db.remove(id);
        indexMap.get(entity.getBoardId()).remove(id);
    }

    public void deleteByBoardId(Long boardId) {
        List<Long> ids = indexMap.get(boardId);
        ids.forEach(db::remove);
    }

    public List<T> findAllByBoardId(Long boardId) {
        List<Long> ids = indexMap.get(boardId);
        return ids.stream()
                .map(db::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<T> findAllByBoardIds(List<Long> boardIds) {
        return boardIds.stream()
                .flatMap(boardId -> {
                    List<Long> ids = indexMap.get(boardId);
                    return ids.stream().map(db::get).filter(Objects::nonNull);
                })
                .collect(Collectors.toList());
    }

    public List<T> findPageByBoardId(Long boardId, int page, int size) {
        List<Long> ids = indexMap.get(boardId);

        return ids.stream()
                .skip((long) (page-1) * size)
                .limit(size)
                .map(db::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public int count() {
        return db.size();
    }
}
