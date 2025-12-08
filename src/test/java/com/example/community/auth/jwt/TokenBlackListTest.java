package com.example.community.auth.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TokenBlackListTest {
    private TokenBlackList tokenBlackList;

    @BeforeEach
    void setUp() {
        tokenBlackList = new TokenBlackList();
    }

    @Test
    void 토큰이_블랙리스트에_추가되고_조회된다() {
        //given
        Long userId = 1L;
        String accessToken = "accessToken";


        //when
        tokenBlackList.add(userId, accessToken);


        //then
        assertThat(tokenBlackList.contains(userId, accessToken)).isTrue();
    }


    @Test
    void 토큰이_존재하지_않으면_false를_반환한다() {
        //given
        Long userId = 1L;
        String accessToken = "accessToken";


        //when
        tokenBlackList.add(userId, accessToken);


        //then
        assertThat(tokenBlackList.contains(2L, "accessToken2")).isFalse();
    }

    @Test
    void 토큰_값으로도_조회할_수_있다() {
        //given
        Long userId = 1L;
        String accessToken = "accessToken";


        //when
        tokenBlackList.add(userId, accessToken);


        //then
        assertThat(tokenBlackList.contains("accessToken")).isTrue();
        assertThat(tokenBlackList.contains("otherAccessToken")).isFalse();
    }
}