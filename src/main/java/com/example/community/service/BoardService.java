package com.example.community.service;

import com.example.community.common.exception.BaseException;
import com.example.community.common.exception.ErrorCode;
import com.example.community.entity.Board;
import com.example.community.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Board post(Long userId, String title, String content, List<Long> boardImageIds) {
        validateTitle(title);
        return boardRepository.save(new Board(title, content, boardImageIds, userId));
    }

    private void validateTitle(String title) {
        if(boardRepository.findByTitle(title).isPresent()) {
            throw new BaseException(ErrorCode.DUPLICATE_TITLE);
        }
    }
}
