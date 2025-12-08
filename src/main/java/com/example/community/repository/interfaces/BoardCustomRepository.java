package com.example.community.repository.interfaces;

import com.example.community.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardCustomRepository {
    Page<Board> findAll(String title, String content, Pageable pageable);
}
