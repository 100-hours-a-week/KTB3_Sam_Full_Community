package com.example.community.auth.jwt;

import com.example.community.auth.userdetails.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TokenProviderTest {
    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private TokenBlackList tokenBlackList;

    @InjectMocks
    private TokenProvider tokenProvider;

    @Test
    void validateToken_블랙리스트면_false를_반환한다() {
        //given
        String accessToken = "accessToken";
        Long userId = 1L;

        given(jwtUtil.extractUserId(accessToken)).willReturn(userId);
        given(tokenBlackList.contains(userId, accessToken)).willReturn(true);


        //when
        boolean result = tokenProvider.validateToken(accessToken);


        //then
        assertThat(result).isFalse();
    }

    @Test
    void validateToken_만료면_false를_반환한다() {
        //given
        String accessToken = "accessToken";
        Long userId = 1L;

        given(jwtUtil.extractUserId(accessToken)).willReturn(userId);
        given(tokenBlackList.contains(userId, accessToken)).willReturn(false);
        given(jwtUtil.isExpired(accessToken)).willReturn(true);


        //when
        boolean result = tokenProvider.validateToken(accessToken);


        //then
        assertThat(result).isFalse();
    }

    @Test
    void validateToken_정상토큰이면_true를_반환한다() {
        //given
        String accessToken = "accessToken";
        Long userId = 1L;

        given(jwtUtil.extractUserId(accessToken)).willReturn(userId);
        given(tokenBlackList.contains(userId, accessToken)).willReturn(false);
        given(jwtUtil.isExpired(accessToken)).willReturn(false);


        //when
        boolean result = tokenProvider.validateToken(accessToken);


        //then
        assertThat(result).isTrue();
    }
}