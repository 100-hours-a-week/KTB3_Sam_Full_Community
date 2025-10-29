package com.example.community.repository;

import com.example.community.entity.Board;
import lombok.Locked;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class BoardRepository extends BaseRepository<Board>{
    @Locked.Read
    public Optional<Board> findByTitle(String title) {
        return db.values().stream()
                .filter(board -> title.equals(board.getTitle()))
                .findFirst();
    }

    @Locked.Read
    public List<Board> findPage(int page, int size) {
        return db.values().stream()
                .skip((long) (page-1) * size)
                .limit(size)
                .collect(Collectors.toList());
    }
}
