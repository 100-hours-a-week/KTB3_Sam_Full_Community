package com.example.community.repository.impl;

import com.example.community.entity.Board;
import com.example.community.repository.interfaces.BoardCustomRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BoardRepositoryImpl implements BoardCustomRepository {
    @Override
    public Optional<Board> findByTitle(String title) {
        return Optional.empty();
    }

    @Override
    public List<Board> findPage(int page, int size) {
        return List.of();
    }
}
