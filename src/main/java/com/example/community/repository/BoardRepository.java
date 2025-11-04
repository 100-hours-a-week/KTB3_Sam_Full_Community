package com.example.community.repository;

import com.example.community.entity.Board;
import com.example.community.repository.interfaces.BoardCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardCustomRepository {
    public Board findByContent(String content);

    @Query("select b from Board b join fetch b.user")
    public Page<Board> findAll(Pageable pageable);

    @Query("select b from Board b join fetch b.user where b.id = :boardId")
    public Optional<Board> findById(@Param("boardId") Long boardId);

    public Optional<Board> findByTitle(String title);

    public void deleteByUserId(Long userId);
}
