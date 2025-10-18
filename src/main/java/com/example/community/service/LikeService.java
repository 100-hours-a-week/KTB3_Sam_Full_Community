package com.example.community.service;

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
}
