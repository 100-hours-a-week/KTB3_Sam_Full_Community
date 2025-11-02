package com.example.community.service;

import com.example.community.common.exception.BaseException;
import com.example.community.common.exception.ErrorCode;
import com.example.community.entity.Board;
import com.example.community.entity.User;
import com.example.community.event.BoardDeletedEvent;
import com.example.community.repository.inmemory.InMemoryBoardRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {
    private final InMemoryBoardRepository inMemoryBoardRepository;
    private final ApplicationEventPublisher eventPublisher;

    BoardService(InMemoryBoardRepository inMemoryBoardRepository, ApplicationEventPublisher eventPublisher) {
        this.inMemoryBoardRepository = inMemoryBoardRepository;
        this.eventPublisher = eventPublisher;
    }

    public Board save(String title, String content, List<Long> boardImageIds, User user) {
        Board board = new Board(title, content, boardImageIds, user);
        user.addPost(board);
        return inMemoryBoardRepository.save(board);
    }

    public void updateBoard(Long boardId, String title, String content, List<Long> boardImageIds) {
        Board board = inMemoryBoardRepository.findById(boardId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_BOARD));

        board.updateBoard(title, content, boardImageIds);
        board.recordModificationTime();
        inMemoryBoardRepository.save(board);
    }

    public void deleteBoard(Long boardId) {
        Board board = inMemoryBoardRepository.findById(boardId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_BOARD));
        inMemoryBoardRepository.deleteById(boardId);
        eventPublisher.publishEvent(new BoardDeletedEvent(boardId));
    }

    public Board findById(Long boardId) {
        return inMemoryBoardRepository.findById(boardId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_BOARD));
    }

    public List<Board> findPage(int page, int size) {
        return inMemoryBoardRepository.findPage(page,size);
    }


    public int count() {
        return inMemoryBoardRepository.count();
    }

    public void validateTitle(String title) {
        if(inMemoryBoardRepository.findByTitle(title).isPresent()) {
            throw new BaseException(ErrorCode.DUPLICATE_TITLE);
        }
    }
}
