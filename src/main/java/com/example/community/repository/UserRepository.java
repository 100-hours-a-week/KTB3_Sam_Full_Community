package com.example.community.repository;

import com.example.community.entity.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepository extends BaseRepository<User>{
    public Optional<User> findByEmail(String email) {
        return db.values().stream()
                .filter(user -> email.equals(user.getEmail()))
                .findFirst();
    }

    public Optional<User> findByNickname(String nickname) {
        return db.values().stream()
                .filter(user -> nickname.equals(user.getNickname()))
                .findFirst();
    }
}
