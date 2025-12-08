package com.example.community.auth.userdetails;

import com.example.community.common.exception.BaseException;
import com.example.community.common.exception.ErrorCode;
import com.example.community.entity.User;
import com.example.community.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;


    @Test
    void 유저_아이디에_해당하는_유저가_존재하는_경우_정상적으로_UserDetails를_반환한다() {
        //given
        Long userId = 10L;
        User user = new User("email", "password", "nickname");
        ReflectionTestUtils.setField(user, "id", userId);

        given(userRepository.findById(10L)).willReturn(Optional.of(user));


        //when
        UserDetails result = customUserDetailsService.loadUserByUsername(userId.toString());


        //then
        assertNotNull(result);
        assertEquals("10", result.getUsername());
    }

    @Test
    void 유저_아이디에_해당하는_유저가_존재하지않는_경우_미리_지정해둔_예외를_반환한다() {
        //given
        Long userId = 99L;
        given(userRepository.findById(99L)).willReturn(Optional.empty());


        //when
        final BaseException result = assertThrows(BaseException.class, () -> customUserDetailsService.loadUserByUsername(userId.toString()));


        //then
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_USER);
    }
}