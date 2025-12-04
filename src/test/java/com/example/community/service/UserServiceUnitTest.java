package com.example.community.service;

import com.example.community.common.exception.BaseException;
import com.example.community.common.exception.ErrorCode;
import com.example.community.entity.User;
import com.example.community.event.UserDeletedEvent;
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
class UserServiceUnitTest {
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
        //given
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
    void 유저_아이디와_비밀번호_그리고_비밀번호확인이_올바른경우_성공적으로_비밀번호가_변경된다() {
        //given
        Long userId = 1L;
        String password = "newPassword";
        String checkPassword = "newPassword";

        User alreadSavedUser = new User("email", "encodedPassword", "nickname");
        given(userRepository.findById(userId)).willReturn(Optional.of(alreadSavedUser));


        //when
        userService.changePassword(userId, password, checkPassword);


        //then
        then(userRepository).should(times(1)).save(any(User.class));
    }

    @Test
    void 비밀번호와_비밀번호확인이_일치하지않는경우_비밀번호가_변경되지_않는다() {
        //given
        Long userId = 1L;
        String password = "newPassword";
        String checkPassword = "notSamePassword";


        //when
        final BaseException result = assertThrows(BaseException.class, () -> userService.changePassword(userId, password, checkPassword));


        //then
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.INVALID_REQUEST);
    }

    @Test
    void 유저_아이디에_해당하는_유저가_존재하는_경우_삭제를_진행한다() {
        //given
        Long userId = 1L;
        User alreadSavedUser = new User("email", "encodedPassword", "nickname");
        given(userRepository.findById(userId)).willReturn(Optional.of(alreadSavedUser));

        //when
        userService.deleteUser(userId);


        //then
        then(userRepository).should(times(1)).deleteById(userId);
        then(eventPublisher).should(times(1)).publishEvent(any(UserDeletedEvent.class));
    }

    @Test
    void 유저_아이디에_해당하는_유저가_존재하지않는_경우_삭제를_진행하지_않는다() {
        //given
        Long userId = 1L;
        User alreadSavedUser = new User("email", "encodedPassword", "nickname");
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        //when
        final BaseException result = assertThrows(BaseException.class, () -> userService.deleteUser(userId));


        //then
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_USER);

    }

    @Test
    void 이메일이_중복될경우_True를_반환한다() {
        //given
        String alreadyExistUserEmail = "email";
        given(userRepository.existByEmail(alreadyExistUserEmail)).willReturn(true);

        //when
        boolean checkEmail = userService.checkEmailDuplicated(alreadyExistUserEmail);


        //then
        assertThat(checkEmail).isEqualTo(Boolean.TRUE);
    }

    @Test
    void 이메일이_중복이_아닐경우_False를_반환한다() {
        //given
        String email = "email";
        given(userRepository.existByEmail(email)).willReturn(false);

        //when
        boolean checkEmail = userService.checkEmailDuplicated(email);

        //then
        assertThat(checkEmail).isEqualTo(Boolean.FALSE);
    }

    @Test
    void 닉네임이_중복인경우_True를_반환한다() {
        //given
        String alreadyExistUserNickname = "nickname";
        given(userRepository.existByNickname(alreadyExistUserNickname)).willReturn(true);

        //when
        boolean checkNickname = userService.checkNicknameDuplicated(alreadyExistUserNickname);

        //then
        assertThat(checkNickname).isEqualTo(Boolean.TRUE);
    }

    @Test
    void 닉네임이_중복이_아닌경우_False를_반환한다() {
        //given
        String nickname = "nickname";
        given(userRepository.existByNickname(nickname)).willReturn(false);

        //when
        boolean checkNickname = userService.checkNicknameDuplicated(nickname);

        //then
        assertThat(checkNickname).isEqualTo(Boolean.FALSE);

    }
}