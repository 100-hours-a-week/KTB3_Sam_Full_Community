package com.example.community.repository;

import com.example.community.entity.Board;
import com.example.community.repository.interfaces.BoardCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardCustomRepository {
    public Board findByContent(String content);

    public Page<Board> findAll(Pageable pageable);

    public Optional<Board> findByTitle(String title);
}
