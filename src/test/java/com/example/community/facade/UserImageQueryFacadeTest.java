package com.example.community.facade;

import com.example.community.dto.response.UserInfoResponse;
import com.example.community.entity.Image;
import com.example.community.entity.User;
import com.example.community.entity.UserImage;
import com.example.community.service.UserImageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserImageQueryFacadeTest {

    @Mock
    private UserImageService userImageService;

    @InjectMocks
    private UserImageQueryFacade userImageQueryFacade;

    @Test
    void 유저_아이디에_해당하는_유저가_있는경우_해당_유저정보와_프로필_이미지를_함께_반환한다() {
        //given
        Long userId = 1L;

        User user = new User("email", "password", "nickname");
        ReflectionTestUtils.setField(user, "id", userId);

        Image image = new Image();
        ReflectionTestUtils.setField(image, "id", 10L);

        UserImage userImage = new UserImage(user, image);

        // findByUserId mock
        given(userImageService.findByUserId(userId)).willReturn(userImage);

        //when
        UserInfoResponse result = userImageQueryFacade.getUser(userId);

        //then
        assertThat(result.userId()).isEqualTo(userId);
        assertThat(result.profileImageId()).isEqualTo(image.getId());

    }
}