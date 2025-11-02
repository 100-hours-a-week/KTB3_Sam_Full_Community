package com.example.community.service;

import com.example.community.common.exception.BaseException;
import com.example.community.common.exception.ErrorCode;
import com.example.community.entity.Board;
import com.example.community.entity.Like;
import com.example.community.entity.User;
import com.example.community.repository.inmemory.InMemoryLikeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikeService {
    private final InMemoryLikeRepository inMemoryLikeRepository;

    LikeService(InMemoryLikeRepository inMemoryLikeRepository) {
        this.inMemoryLikeRepository = inMemoryLikeRepository;
    }

    public Like save(User user, Board board) {
        return new Like(user,board);
    }

    public List<Like> findAllByBoardIds(List<Long> boardIds) {
        return inMemoryLikeRepository.findAllByBoardIds(boardIds);
    }

    public List<Like> findAllByBoardId(Long boardId) {
        return inMemoryLikeRepository.findAllByBoardId(boardId);
    }

    public void deleteByBoardId(Long boardId) {
        inMemoryLikeRepository.deleteByBoardId(boardId);
    }


    public void deleteLike(Long userId, Long boardId) {
        Like like = inMemoryLikeRepository.findByUserIdAndBoardId(userId, boardId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_LIKE));

        inMemoryLikeRepository.deleteById(like.getId());
    }

    public Optional<Like> findByUserIdAndBoardId(Long userId, Long boardId) {
        return inMemoryLikeRepository.findByUserIdAndBoardId(userId, boardId);
    }
}
