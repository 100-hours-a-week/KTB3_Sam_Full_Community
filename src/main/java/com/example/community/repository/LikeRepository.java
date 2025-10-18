package com.example.community.repository;

import com.example.community.entity.Like;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.Map;

@Repository
public class LikeRepository {
    private Map<Long, Like> likeDB;
    private long sequence = 0L;

    LikeRepository() {
        this.likeDB = new LinkedHashMap<>();
    }
}
