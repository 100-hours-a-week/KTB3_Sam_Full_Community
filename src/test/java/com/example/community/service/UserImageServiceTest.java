package com.example.community.service;

import com.example.community.common.exception.BaseException;
import com.example.community.common.exception.ErrorCode;
import com.example.community.entity.Image;
import com.example.community.entity.User;
import com.example.community.entity.UserImage;
import com.example.community.repository.UserImageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserImageServiceTest {
    @Mock
    private UserImageRepository userImageRepository;

    @InjectMocks
    private UserImageService userImageService;

    @Test
    void 기존_유저_이미지_존재시_기존_이미지와_다른_이미지를_등록할경우_기존_이미지를_삭제_후_재등록한다() {
        //given
        User alreadySavedUser = new User("email", "password", "nickname");
        ReflectionTestUtils.setField(alreadySavedUser, "id", 1L);

        Image alreadySavedImage = new Image();
        ReflectionTestUtils.setField(alreadySavedImage, "id", 100L);

        UserImage userImage = new UserImage(alreadySavedUser, alreadySavedImage);

        Image newImage = new Image();

        given(userImageRepository.findByUserId(alreadySavedUser.getId())).willReturn(Optional.of(userImage));

        given(userImageRepository.save(any(UserImage.class))).willReturn(new UserImage(alreadySavedUser, newImage));


        //when
        userImageService.save(alreadySavedUser, newImage);


        //then
        then(userImageRepository).should(times(1)).deleteByUserId(alreadySavedUser.getId());
    }

    @Test
    void 기존_유저_이미지_존재시_기존_이미지와_같은_이미지를_등록할경우_기존_이미지가_등록된_상태_그대로둔다() {
        //given
        User user = new User("email", "password", "nickname");
        ReflectionTestUtils.setField(user, "id", 1L);

        Image sameImage = new Image();
        ReflectionTestUtils.setField(sameImage, "id", 10L);

        UserImage existingUserImage = new UserImage(user, sameImage);

        // 기존 이미지 존재 + 새 이미지 ID 동일
        given(userImageRepository.findByUserId(1L))
                .willReturn(Optional.of(existingUserImage));

        //when
        UserImage result = userImageService.save(user, sameImage);

        //then
        assertThat(result).isSameAs(existingUserImage);
    }

    @Test
    void 기존_유저_이미지_미존재시_해당_유저_이미지로_바로_등록된다() {
        //given
        User user = new User("email", "password", "nickname");
        ReflectionTestUtils.setField(user, "id", 1L);

        Image newImage = new Image();
        ReflectionTestUtils.setField(newImage, "id", 20L);

        given(userImageRepository.findByUserId(1L))
                .willReturn(Optional.empty());

        UserImage newSavedImage = new UserImage(user, newImage);
        given(userImageRepository.save(any(UserImage.class)))
                .willReturn(newSavedImage);


        //when
        UserImage result = userImageService.save(user, newImage);


        //then
        assertThat(result.getImage().getId()).isEqualTo(20L);
    }

    @Test
    void 유저_아이디에_해당하는_유저_이미지가_있는경우_해당_유저_이미지를_반환한다() {
        //given
        Long userId = 1L;

        User user = new User("email", "password", "nickname");
        ReflectionTestUtils.setField(user, "id", userId);

        Image image = new Image();
        ReflectionTestUtils.setField(image, "id", 10L);

        UserImage userImage = new UserImage(user, image);

        given(userImageRepository.findByUserId(userId))
                .willReturn(Optional.of(userImage));


        //when
        UserImage result = userImageService.findByUserId(userId);


        //then
        assertThat(result).isSameAs(userImage);
    }

    @Test
    void 유저_아이디에_해당하는_유저_이미지가_없는경우_지정해둔_에러를_반환한다() {
        //given
        Long userId = 1L;

        given(userImageRepository.findByUserId(userId))
                .willReturn(Optional.empty());

        //when
        final BaseException result = assertThrows(BaseException.class,
                () -> userImageService.findByUserId(userId));


        //then
        assertThat(result.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_USER_IMAGE);
    }

    @Test
    void 유저_아이디에_해당하는_유저_이미지가_있는경우_삭제를_진행한다() {
        //given
        Long userId = 1L;


        //when
        userImageService.deleteByUserId(userId);


        //then
        then(userImageRepository).should(times(1)).deleteByUserId(userId);
    }
}