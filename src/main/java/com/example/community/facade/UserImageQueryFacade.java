package com.example.community.facade;

import com.example.community.dto.response.UserInfoResponse;
import com.example.community.entity.UserImage;
import com.example.community.service.ImageService;
import com.example.community.service.UserImageService;
import com.example.community.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserImageQueryFacade {
    private final UserImageService userImageService;

    UserImageQueryFacade(UserImageService userImageService) {
        this.userImageService = userImageService;
    }

    public UserInfoResponse getUser(Long userId) {
        UserImage userImage = userImageService.findByUserId(userId);

        return UserInfoResponse.of(userImage.getUser(), userImage.getImage());
    }
}
