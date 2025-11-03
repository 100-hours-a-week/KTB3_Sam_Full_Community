package com.example.community.service;

import com.example.community.common.exception.BaseException;
import com.example.community.common.exception.ErrorCode;
import com.example.community.entity.Board;
import com.example.community.entity.Like;
import com.example.community.entity.User;
import com.example.community.repository.LikeRepository;
import org.springframework.data.domain.Page;
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
        Like like = new Like(user,board);
        board.addLike(like);
        return likeRepository.save(new Like(user,board));
    }

    public List<Like> findAllByPagedBoards(Page<Board> posts) {
        return likeRepository.findAllByPost(posts);
    }

    public List<Like> findAllByBoard(Board post) {
        return likeRepository.findAllByPost(post);
    }

    public void deleteByBoard(Board post) {
        likeRepository.deleteByPost(post);
    }


    public void deleteLikeByUserAndPost(User user, Board post) {
        Like like = likeRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_LIKE));
        post.removeLike(like);
        likeRepository.deleteById(like.getId());
    }

    public Optional<Like> findByUserAndPost(User user, Board post) {
        return likeRepository.findByUserAndPost(user,post);
    }
}
