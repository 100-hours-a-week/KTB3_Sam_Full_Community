package com.example.community.service;

import com.example.community.common.exception.BaseException;
import com.example.community.common.exception.ErrorCode;
import com.example.community.entity.Like;
import com.example.community.repository.LikeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeService {
    private final LikeRepository likeRepository;

    LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
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

    public Like postLike(Long userId, Long boardId) {
        if(likeRepository.findByUserIdAndBoardId(userId, boardId).isPresent()) {
            throw new BaseException(ErrorCode.ALREADY_LIKED_POST);
        }
        return likeRepository.save(new Like(userId, boardId));
    }

    public void deleteLike(Long userId, Long boardId) {
        Like like = likeRepository.findByUserIdAndBoardId(userId, boardId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_LIKE));

        likeRepository.deleteById(like.getId());
    }
}
