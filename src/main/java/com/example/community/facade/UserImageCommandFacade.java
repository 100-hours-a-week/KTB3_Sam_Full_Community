package com.example.community.facade;

import com.example.community.entity.Image;
import com.example.community.entity.User;
import com.example.community.entity.UserImage;
import com.example.community.service.ImageService;
import com.example.community.service.UserImageService;
import com.example.community.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserImageCommandFacade {
    private final UserService userService;
    private final ImageService imageService;
    private final UserImageService userImageService;

    UserImageCommandFacade(UserService userService, ImageService imageService, UserImageService userImageService) {
        this.userService = userService;
        this.imageService = imageService;
        this.userImageService = userImageService;
    }

    public void mapsImagesToUser(Long userId, Long profileImageId) {
        User user = userService.getUser(userId);
        Optional<Image> image = imageService.findById(profileImageId);

        image.ifPresent(value -> userImageService.save(user, value));
    }
}
