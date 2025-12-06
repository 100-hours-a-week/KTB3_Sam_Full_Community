package com.example.community.repository.impl;

import com.example.community.QueryDslTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(QueryDslTestConfig.class)
class BoardRepositoryImplTest {

    @Test
    void findAll() {
    }
}