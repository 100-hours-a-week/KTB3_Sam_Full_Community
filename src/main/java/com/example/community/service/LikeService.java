package com.example.community.service;

import com.example.community.common.exception.BaseException;
import com.example.community.common.exception.ErrorCode;
import com.example.community.entity.Board;
import com.example.community.entity.Like;
import com.example.community.entity.User;
import com.example.community.repository.LikeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikeService {
    private final LikeRepository likeRepository;

    LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public Like save(User user, Board board) {
        return new Like(user,board);
    }

    public List<Like> findAllByBoardIds(List<Long> boardIds) {
        return likeRepository.findAllByBoardIds(boardIds);
    }

    public List<Like> findAllByBoardId(Long boardId) {
        return likeRepository.findAllByBoardId(boardId);
    }

    public void deleteByBoardId(Long boardId) {
        likeRepository.deleteByBoardId(boardId);
    }


    public void deleteLike(Long userId, Long boardId) {
        Like like = likeRepository.findByUserIdAndBoardId(userId, boardId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_LIKE));

        likeRepository.deleteById(like.getId());
    }

    public Optional<Like> findByUserIdAndBoardId(Long userId, Long boardId) {
        return likeRepository.findByUserIdAndBoardId(userId, boardId);
    }
}
