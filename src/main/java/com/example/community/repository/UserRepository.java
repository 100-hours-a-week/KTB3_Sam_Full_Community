package com.example.community.repository;

import com.example.community.entity.User;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.Map;

@Repository
public class UserRepository {
    private Map<Long, User> userDB;

    UserRepository() {
        this.userDB = new LinkedHashMap<>();
    }
}
