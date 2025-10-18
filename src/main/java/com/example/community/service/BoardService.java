package com.example.community.service;

import com.example.community.common.exception.BaseException;
import com.example.community.common.exception.ErrorCode;
import com.example.community.entity.Board;
import com.example.community.repository.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Transactional
    public Board post(Long userId, String title, String content, List<Long> boardImageIds) {
        validateTitle(title);
        return boardRepository.save(new Board(title, content, boardImageIds, userId));
    }

    public void updateBoard(Long boardId, String title, String content, List<Long> boardImageIds) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_BOARD));

        board.updateBoard(title, content, boardImageIds);
        boardRepository.save(board);
    }

    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_BOARD));
        boardRepository.deleteById(boardId);
    }

    public Board findById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_BOARD));
    }

    public List<Board> findPage(int page, int size) {
        return boardRepository.findPage(page,size);
    }


    public int count() {
        return boardRepository.count();
    }

    private void validateTitle(String title) {
        if(boardRepository.findByTitle(title).isPresent()) {
            throw new BaseException(ErrorCode.DUPLICATE_TITLE);
        }
    }
}
