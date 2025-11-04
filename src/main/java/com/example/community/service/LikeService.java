package com.example.community.service;

import com.example.community.common.exception.BaseException;
import com.example.community.common.exception.ErrorCode;
import com.example.community.entity.Board;
import com.example.community.entity.Like;
import com.example.community.entity.User;
import com.example.community.repository.LikeRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LikeService {
    private final LikeRepository likeRepository;

    LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public Like save(User user, Board board) {
        Like like = new Like(user,board);
        return likeRepository.save(new Like(user,board));
    }

    public List<Like> findAllByPagedBoardIds(List<Long> boardIds) {
        return likeRepository.findAllByBoardId(boardIds);
    }

    public List<Like> findAllByBoard(Long boardId) {
        return likeRepository.findAllByBoardId(boardId);
    }

    @Transactional
    public void deleteByBoardId(Long boardId) {
        likeRepository.deleteByBoardId(boardId);
    }

    @Transactional
    public void deleteByUserIdAndBoardId(Long userId, Long boardId) {
        Like like = likeRepository.findByUserIdAndBoardId(userId, boardId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_LIKE));
        likeRepository.deleteById(like.getId());
    }

    public Optional<Like> findByUserIdAndBoardId(Long userId, Long boardId) {
        return likeRepository.findByUserIdAndBoardId(userId, boardId);
    }
}
