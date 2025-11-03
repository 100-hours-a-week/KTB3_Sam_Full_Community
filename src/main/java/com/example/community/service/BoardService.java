package com.example.community.service;

import com.example.community.common.exception.BaseException;
import com.example.community.common.exception.ErrorCode;
import com.example.community.entity.Board;
import com.example.community.entity.User;
import com.example.community.event.BoardDeletedEvent;
import com.example.community.repository.BoardRepository;
import com.example.community.repository.inmemory.InMemoryBoardRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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
        Board board = new Board(title, content, boardImageIds, user);
        user.addPost(board);
        return boardRepository.save(board);
    }

    public void updateBoard(Long boardId, String title, String content, List<Long> boardImageIds) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_BOARD));

        board.updateBoard(title, content, boardImageIds);
        board.recordModificationTime();
        boardRepository.save(board);
    }

    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_BOARD));
        boardRepository.deleteById(boardId);
        eventPublisher.publishEvent(new BoardDeletedEvent(boardId));
    }

    public Board findById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_BOARD));
    }

    public Page<Board> findPage(int page, int size) {
        return boardRepository.findAll(PageRequest.of(page,size));
    }


    public int count() {
        return Long.valueOf(boardRepository.count()).intValue();
    }

    public void validateTitle(String title) {
        if(boardRepository.findByTitle(title).isPresent()) {
            throw new BaseException(ErrorCode.DUPLICATE_TITLE);
        }
    }
}
