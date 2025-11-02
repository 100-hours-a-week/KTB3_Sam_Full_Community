package com.example.community.repository;

import com.example.community.entity.BaseEntity;
import com.example.community.entity.interfaces.BoardLinked;
import com.example.community.entity.interfaces.Identifiable;
import com.example.community.repository.interfaces.BoardAggregateRepository;
import com.example.community.repository.interfaces.CRUDRepository;
import lombok.Locked;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class BoardLinkedRepository<T extends BaseEntity & BoardLinked & Identifiable> implements CRUDRepository<T>, BoardAggregateRepository<T> {
    protected Map<Long, T> db = new LinkedHashMap<>();
    protected long sequence = 0L;
    protected Map<Long, List<Long>> indexMap = new ConcurrentHashMap<>();

    @Locked.Write
    public T save(T entity) {
        if(entity.getId() == null) {
            entity.setId(++sequence);
        }

        db.put(entity.getId(), entity);

        List<Long> entityIdList = indexMap.computeIfAbsent(entity.getBoardId(), key -> new ArrayList<>());
        entityIdList.add(entity.getId());

        return entity;
    }

    @Locked.Read
    public Optional<T> findById(Long id) {
        return Optional.ofNullable(db.get(id));
    }

    @Locked.Write
    public void deleteById(Long id) {
        T entity = db.remove(id);

        indexMap.computeIfPresent(entity.getBoardId(), (boardId, list) -> {
            list.remove(entity.getId());
            return list;
        });
    }

    @Locked.Write
    public void deleteByBoardId(Long boardId) {
        List<Long> ids = indexMap.remove(boardId);
        ids.forEach(db::remove);
    }

    @Locked.Read
    public List<T> findAllByBoardId(Long boardId) {
        List<Long> ids = indexMap.get(boardId);

        if ( ids == null ) return List.of();

        return ids.stream()
                .map(db::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Locked.Read
    public List<T> findAllByBoardIds(List<Long> boardIds) {
        List<Long> ids = boardIds.stream()
                .map(boardId -> indexMap.getOrDefault(boardId, List.of()))
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .toList();

        if(ids.isEmpty()) return List.of();

        return ids.stream().map(db::get).filter(Objects::nonNull).toList();
    }

    @Locked.Read
    public List<T> findPageByBoardId(Long boardId, int page, int size) {
        List<Long> ids = indexMap.get(boardId);

        if(ids == null) return List.of();

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
