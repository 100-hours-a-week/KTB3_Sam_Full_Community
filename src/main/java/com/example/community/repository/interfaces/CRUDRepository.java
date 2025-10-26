package com.example.community.repository.interfaces;

import java.util.Optional;

public interface CRUDRepository<T> {
    public T save(T entity);

    public Optional<T> findById(Long id);

    public void deleteById(Long id);
}
