package com.example.community.service;

import com.example.community.common.exception.BaseException;
import com.example.community.common.exception.ErrorCode;
import com.example.community.entity.User;
import com.example.community.event.UserSavedEvent;
import com.example.community.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private UserService userService;

    @Test
    void 이메일_패스워드_닉네임_프로필이미지를_입력할경우_회원가입이_성공적으로_진행된다() {
        //given
        String email = "test@email.com";
        String password = "testPassword";
        String nickname = "testNickname";
        Long profileImageId = 3L;

        given(userRepository.findByEmail(email)).willReturn(Optional.empty());
        given(userRepository.findByNickname(nickname)).willReturn(Optional.empty());

        given(passwordEncoder.encode(password)).willReturn("encodedPw");

        User savedUser = new User(email, "encodedPw", nickname);
        given(userRepository.save(any(User.class))).willReturn(savedUser);


        //when
        User result = userService.registerUser(email, password, nickname, profileImageId);


        //then
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getPassword()).isEqualTo("encodedPw");
        assertThat(result.getNickname()).isEqualTo(nickname);

        then(eventPublisher).should(times(1)).publishEvent(any(UserSavedEvent.class));
    }

    @Test
    void 중복된_이메일이_있을_경우_회원가입이_진행되지_않는다() {
        //given
        String email = "test@email.com";
        String password = "testPassword";
        String nickname = "testNickname";
        Long profileImageId = 3L;

        given(userRepository.findByEmail(email))
                .willReturn(Optional.of(new User(email, "otherPassword", "otherNickname")));


        //when
        final BaseException result = assertThrows(BaseException.class, () -> userService.registerUser(email, password, nickname, profileImageId));


        //then
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.ALREADY_REGISTERED_EMAIL);

    }


    @Test
    void 중복된_닉네임이_있을_경우_회원가입이_진행되지_않는다() {
        //given
        String email = "test@email.com";
        String password = "testPassword";
        String nickname = "testNickname";
        Long profileImageId = 3L;

        given(userRepository.findByNickname(nickname))
                .willReturn(Optional.of(new User("otherEmail", "otherPassword", nickname)));


        //when
        final BaseException result = assertThrows(BaseException.class, () -> userService.registerUser(email, password, nickname, profileImageId));


        //then
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.ALREADY_REGISTERED_NICKNAME);
    }


    @Test
    void 유저_아이디에_해당하는_유저가_있을경우_반환한다() {
        //given
        Long userId = 1L;
        User alreadySavedUser = new User("email", "encodedPassword", "nickname");
        ReflectionTestUtils.setField(alreadySavedUser, "id", userId);

        given(userRepository.findById(userId)).willReturn(Optional.of(alreadySavedUser));


        //when
        User result = userService.getUser(userId);


        //then
        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getEmail()).isEqualTo("email");
        assertThat(result.getPassword()).isEqualTo("encodedPassword");
    }

    @Test
    void 유저_아이디에_해당하는_유저가_없을경우_예외를_반환한다() {
        //given
        Long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        //when
        final BaseException result = assertThrows(BaseException.class, () -> userService.getUser(userId));


        //then
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_USER);
    }

    @Test
    void 유저_아이디_닉네임_프로필이미지_아이디를_모두_입력받은경우_사용자_정보가_성공적으로_수정된다() {
        //give
        Long userId = 1L;
        String newNickname = "newnickname";
        Long newProfileImageId = 100L;

        User alreadSavedUser = new User("email", "encodedPassword", "nickname");
        given(userRepository.findById(userId)).willReturn(Optional.of(alreadSavedUser));
        //given(userRepository.save())

        //when
        userService.modifyUser(userId, newNickname,newProfileImageId);

        //then
        then(userRepository).should(times(1)).save(any(User.class));
        then(eventPublisher).should(times(1)).publishEvent(any(UserSavedEvent.class));
    }

    @Test
    void changePassword() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void checkEmailDuplicated() {
    }

    @Test
    void checkNicknameDuplicated() {
    }
}