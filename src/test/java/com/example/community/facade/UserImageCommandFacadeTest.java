package com.example.community.facade;

import com.example.community.entity.Image;
import com.example.community.entity.User;
import com.example.community.service.ImageService;
import com.example.community.service.UserImageService;
import com.example.community.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserImageCommandFacadeTest {
    @Mock
    UserService userService;

    @Mock
    ImageService imageService;

    @Mock
    UserImageService userImageService;

    @InjectMocks
    UserImageCommandFacade userImageCommandFacade;

    @Test
    void mapsImagesToUser() {
        //given
        Long userId = 1L;
        Long profileImageId = 100L;

        User user = new User("email", "password", "nickname");
        ReflectionTestUtils.setField(user, "id", userId);

        Image image = new Image();
        ReflectionTestUtils.setField(image, "id", profileImageId);

        given(userService.getUser(userId)).willReturn(user);
        given(imageService.findById(profileImageId)).willReturn(image);


        //when
        userImageCommandFacade.mapsImagesToUser(userId, profileImageId);


        //then
        then(userImageService).should(times(1)).save(user, image);
    }
}