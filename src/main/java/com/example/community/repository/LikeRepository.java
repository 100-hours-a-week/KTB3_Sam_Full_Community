package com.example.community.repository;

import com.example.community.entity.Board;
import com.example.community.entity.Like;
import com.example.community.entity.User;
import com.example.community.repository.interfaces.LikeCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long>, LikeCustomRepository {
    public Optional<Like> findByUserIdAndBoardId(Long userId, Long boardId);

    public List<Like> findAllByBoardId(List<Long> boardIds);

    public List<Like> findAllByBoardId(Long boardId);

    public void deleteByBoardId(Long boardId);
}
