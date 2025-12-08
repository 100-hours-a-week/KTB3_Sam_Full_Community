package com.example.community.service;

import com.example.community.auth.jwt.JwtUtil;
import com.example.community.auth.jwt.TokenBlackList;
import com.example.community.common.exception.BaseException;
import com.example.community.common.exception.ErrorCode;
import com.example.community.dto.AuthToken;
import com.example.community.entity.User;
import com.example.community.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private TokenBlackList tokenBlackList;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void 로그인_성공시_accessToken과_refreshToken을_발급한다() {
        //given
        String email = "email";
        String password = "password";

        User savedUser = new User(email, "encodedPassword", "nickname");
        ReflectionTestUtils.setField(savedUser, "id", 1L);

        given(userRepository.findByEmail(email)).willReturn(Optional.of(savedUser));
        given(passwordEncoder.matches(password, savedUser.getPassword())).willReturn(true);

        given(jwtUtil.generateAccessToken(savedUser.getId())).willReturn("accessToken");
        given(jwtUtil.generateRefreshToken(savedUser.getId())).willReturn("refreshToken");


        //when
        AuthToken token = authService.login(email, password);


        //then
        assertThat(token.accessToken()).isEqualTo("accessToken");
        assertThat(token.refreshToken()).isEqualTo("refreshToken");
    }

    @Test
    void 로그인시_비밀번호가_다르면_지정해둔_예외를_반환한다() {
        //given
        String email = "email";
        String password = "wrongPassword";

        User savedUser = new User(email, "encodedPassword", "nickname");
        ReflectionTestUtils.setField(savedUser, "id", 1L);

        given(userRepository.findByEmail(email)).willReturn(Optional.of(savedUser));
        given(passwordEncoder.matches(password, savedUser.getPassword())).willReturn(false);


        //when
        final BaseException result = assertThrows(BaseException.class, () ->  authService.login(email, password));


        //then
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.INVALID_PASSWORD);
    }

    @Test
    void 로그아웃이_성공적으로_이루어질경우_BlackList에_토큰이_등록된다() {
        //given
        Long userId = 1L;
        String token = "accessToken";

        User savedUser = new User("email", "encoded", "nickname");
        ReflectionTestUtils.setField(savedUser, "id", userId);

        given(userRepository.findById(userId)).willReturn(Optional.of(savedUser));


        //when
        authService.logout(userId, token);


        //then
        then(tokenBlackList).should(times(1)).add(userId, token);
    }

    @Test
    void 토큰_재발급_성공시_새로운_accessToken과_새로운_refreshToken을_반환한다() {
        //given
        Long userId = 1L;
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";

        given(jwtUtil.isExpired(refreshToken)).willReturn(false);
        given(tokenBlackList.contains(accessToken)).willReturn(false);

        given(jwtUtil.generateAccessToken(userId)).willReturn("newAccessToken");
        given(jwtUtil.generateRefreshToken(userId)).willReturn("newRefreshToken");


        //when
        AuthToken newToken = authService.reissue(userId, accessToken, refreshToken);


        //then
        assertThat(newToken.accessToken()).isEqualTo("newAccessToken");
        assertThat(newToken.refreshToken()).isEqualTo("newRefreshToken");
    }

    @Test
    void 토큰_재발급시_refreshToken이_만료되면_지정해둔_예외를_반환한다() {
        //given
        Long userId = 1L;
        String accessToken = "accessToken";
        String expiredRefreshToken = "refreshToken";

        given(jwtUtil.isExpired(expiredRefreshToken)).willReturn(true);


        //when
        final BaseException result = assertThrows(BaseException.class, () -> authService.reissue(userId, accessToken, expiredRefreshToken));


        //then
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.INVALID_REQUEST);
    }


    @Test
    void 토큰_재발급시_accessToken이_블랙리스트에_있으면_지정해둔_예외를_반환한다() {
        //given
        Long userId = 1L;
        String blackedAccessToken = "accessToken";
        String refreshToken = "refreshToken";

        given(jwtUtil.isExpired(refreshToken)).willReturn(false);
        given(tokenBlackList.contains(blackedAccessToken)).willReturn(true);


        //when
        final BaseException result = assertThrows(BaseException.class, () -> authService.reissue(userId, blackedAccessToken, refreshToken));


        //then
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.TOKEN_INVALID);
    }
}