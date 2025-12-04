package com.example.community.repository.impl;

import com.example.community.QueryDslTestConfig;
import com.example.community.entity.User;
import com.example.community.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(QueryDslTestConfig.class)
class UserRepositoryImplTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager em;

    @Test
    void findByEmail() {
    }

    @Test
    void findByNickname() {
    }

    @Test
    void existByEmail() {
    }

    @Test
    void existByNickname() {
    }
}