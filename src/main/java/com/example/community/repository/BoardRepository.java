package com.example.community.repository;

import com.example.community.entity.Board;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class BoardRepository {
    private Map<Long, Board> boardDB;
    private long sequence = 0L;

    BoardRepository() {
        this.boardDB = new LinkedHashMap<>();
    }

    public Board save(Board board) {
        if(board.getId() == null) {
            board.setId(++sequence);
        }
        boardDB.put(board.getId(), board);
        return board;
    }

    public List<Board> findAll() {
        return new ArrayList<>(boardDB.values());
    }

    public Optional<Board> findById(Long id) {
        return Optional.ofNullable(boardDB.get(id));
    }

    public void deleteById(Long id) {
        boardDB.remove(id);
    }

    public Optional<Board> findByTitle(String title) {
        return boardDB.values().stream()
                .filter(board -> title.equals(board.getTitle()))
                .findFirst();
    }
}
