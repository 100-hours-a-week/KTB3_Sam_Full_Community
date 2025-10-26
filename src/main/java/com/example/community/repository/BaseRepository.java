package com.example.community.repository;

import com.example.community.entity.BaseEntity;
import com.example.community.repository.interfaces.CRUDRepository;

import java.util.*;
import java.util.stream.Collectors;

public class BaseRepository<T extends BaseEntity> implements CRUDRepository<T> {
    protected Map<Long, T> db = new LinkedHashMap<>();
    protected long sequence = 0L;

    public T save(T entity) {
        if(entity.getId() == null) {
            entity.setId(++sequence);
        }
        db.put(entity.getId(), entity);
        return entity;
    }

    public Optional<T> findById(Long id) {
        return Optional.ofNullable(db.get(id));
    }

    public List<T> findByIds(List<Long> ids) {
        return ids.stream()
                .map(db::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id){
        db.remove(id);
    }

    public int count() {
        return db.size();
    }


}
