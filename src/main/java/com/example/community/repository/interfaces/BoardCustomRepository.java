package com.example.community.repository.interfaces;

import com.example.community.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;

public interface BoardCustomRepository {
    Page<Board> findAll(String title, String content, Pageable pageable);
}
