package com.example.community.repository;

import com.example.community.entity.Like;
import com.example.community.repository.interfaces.LikeCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long>, LikeCustomRepository {
}
