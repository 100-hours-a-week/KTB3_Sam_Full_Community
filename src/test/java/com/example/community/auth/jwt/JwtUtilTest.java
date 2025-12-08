package com.example.community.auth.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        String secret = "a".repeat(32);
        long accessMs = 1000L * 60 * 10;
        long refreshMs = 1000L * 60 * 60;
        jwtUtil = new JwtUtil(secret, accessMs, refreshMs);
    }

    @Test
    void 액세스토큰이_정상적으로_생성되고_userId를_추출할_수_있다() {
        //given
        String accessToken = jwtUtil.generateAccessToken(10L);


        //when
        Long userId = jwtUtil.extractUserId(accessToken);


        //then
        assertThat(userId).isEqualTo(10L);
    }

    @Test
    void 리프레시토큰이_정상적으로_생성되고_userId를_추출할_수_있다() {
        //given
        String refreshToken = jwtUtil.generateRefreshToken(10L);


        //when
        Long userId = jwtUtil.extractUserId(refreshToken);


        //then
        assertThat(userId).isEqualTo(10L);
    }
}