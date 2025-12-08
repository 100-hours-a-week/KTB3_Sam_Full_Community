package com.example.community.repository.inmemory;

import com.example.community.entity.BaseEntity;
import com.example.community.entity.interfaces.Identifiable;
import com.example.community.repository.inmemory.interfaces.CRUDRepository;
import lombok.Locked;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryBaseRepository<T extends BaseEntity & Identifiable> implements CRUDRepository<T> {
    protected Map<Long, T> db = new LinkedHashMap<>();
    protected long sequence = 0L;

    @Locked.Write
    public T save(T entity) {
        if(entity.getId() == null) {
            entity.setId(++sequence);
        }
        db.put(entity.getId(), entity);
        return entity;
    }

    @Locked.Read
    public Optional<T> findById(Long id) {
        return Optional.ofNullable(db.get(id));
    }

    @Locked.Read
    public List<T> findByIds(List<Long> ids) {
        return ids.stream()
                .map(db::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Locked.Write
    public void deleteById(Long id){
        db.remove(id);
    }

    @Locked.Read
    public int count() {
        return db.size();
    }


}
