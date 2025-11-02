package com.example.community.repository;

import com.example.community.entity.User;
import com.example.community.repository.interfaces.UserCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository {
}
