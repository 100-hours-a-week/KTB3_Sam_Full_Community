package com.example.community.repository.impl;

import com.example.community.QueryDslTestConfig;
import com.example.community.entity.User;
import com.example.community.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(QueryDslTestConfig.class)
class UserRepositoryImplTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager em;

    @BeforeEach
    void setUpUser() {
        //given
        User user = new User("email", "password", "nickname");
        em.persist(user);
        em.flush();
        em.clear();
    }

    @Test
    void findByEmail() {
        //when
        Optional<User> result = userRepository.findByEmail("email");

        //then
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("email");
    }

    @Test
    void findByNickname() {
        //when
        Optional<User> result = userRepository.findByNickname("nickname");

        //then
        assertThat(result).isPresent();
        assertThat(result.get().getNickname()).isEqualTo("nickname");

    }

    @Test
    void existByEmailTrue() {
        //when
        boolean result = userRepository.existByEmail("email");

        //then
        assertThat(result).isEqualTo(Boolean.TRUE);
    }

    @Test
    void existByEmailFalse() {
        //when
        boolean result = userRepository.existByEmail("not-exist-email");

        //then
        assertThat(result).isEqualTo(Boolean.FALSE);
    }

    @Test
    void existByNicknameTrue() {
        //when
        boolean result = userRepository.existByNickname("nickname");

        //then
        assertThat(result).isEqualTo(Boolean.TRUE);
    }

    @Test
    void existByNicknameFalse() {
        //when
        boolean result = userRepository.existByNickname("not-exist-nickname");

        //then
        assertThat(result).isEqualTo(Boolean.FALSE);
    }
}