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
    public Optional<Like> findByUserAndPost(User user, Board post);

    public List<Like> findAllByPost(Page<Board> posts);

    public List<Like> findAllByPost(Board post);

    public void deleteByPost(Board post);
}
