package com.example.community.repository;

import com.example.community.entity.Board;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.Map;

@Repository
public class BoardRepository {
    private final Map<Long, Board> boardDB;

    BoardRepository() {
        this.boardDB = new LinkedHashMap<>();
    }
}
