package com.example.community.service;

import com.example.community.common.exception.BaseException;
import com.example.community.common.exception.ErrorCode;
import com.example.community.entity.Board;
import com.example.community.entity.User;
import com.example.community.event.BoardSavedEvent;
import com.example.community.repository.BoardRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final ApplicationEventPublisher eventPublisher;

    BoardService(BoardRepository boardRepository, ApplicationEventPublisher eventPublisher) {
        this.boardRepository = boardRepository;
        this.eventPublisher = eventPublisher;
    }

    public Board save(String title, String content, List<Long> boardImageIds, User user) {
        Board board = boardRepository.save(new Board(title,content,user));

        eventPublisher.publishEvent(new BoardSavedEvent(board.getId(), boardImageIds));

        return board;
    }

    @Transactional
    public void update(Long userId, Long boardId, String title, String content, List<Long> boardImageIds) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_BOARD));

        validateUser(board, userId);

        board.updateBoard(title, content);
        boardRepository.save(board);

        eventPublisher.publishEvent(new BoardSavedEvent(board.getId(), boardImageIds));
    }

    @Transactional
    public void delete(Long userId, Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_BOARD));

        validateUser(board,userId);

        boardRepository.deleteById(boardId);
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        boardRepository.deleteByUserId(userId);
    }

    public Board findById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_BOARD));
    }

    public Page<Board> findPage(String title, String content, int page, int size) {
        return boardRepository.findAll(title,content, PageRequest.of(page-1,size));
    }

    public void validateTitle(String title) {
        if(boardRepository.findByTitle(title).isPresent()) {
            throw new BaseException(ErrorCode.DUPLICATE_TITLE);
        }
    }

    private void validateUser(Board board, Long userId) {
        if(!board.getUser().getId().equals(userId)) {
            throw new BaseException(ErrorCode.INVALID_REQUEST);
        }
    }
}
