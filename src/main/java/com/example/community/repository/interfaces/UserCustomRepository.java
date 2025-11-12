package com.example.community.repository.interfaces;

import com.example.community.entity.User;

import java.util.Optional;

public interface UserCustomRepository {
    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    Boolean existByEmail(String email);
}
