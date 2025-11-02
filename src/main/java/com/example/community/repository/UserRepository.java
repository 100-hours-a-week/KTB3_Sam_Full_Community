package com.example.community.repository;

import com.example.community.entity.User;
import com.example.community.repository.interfaces.UserCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository {
    public Optional<User> findByEmail(String email);

    public Optional<User> findByNickname(String nickname);
}
