package com.example.community.repository;

import com.example.community.entity.Board;
import com.example.community.repository.interfaces.BoardCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardCustomRepository {
}
