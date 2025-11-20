package com.example.community.service;

import com.example.community.common.exception.BaseException;
import com.example.community.common.exception.ErrorCode;
import com.example.community.entity.Image;
import com.example.community.entity.User;
import com.example.community.entity.UserImage;
import com.example.community.repository.UserImageRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserImageService {
    private final UserImageRepository userImageRepository;

    UserImageService(UserImageRepository userImageRepository) {
        this.userImageRepository = userImageRepository;
    }

    public UserImage save(User user, Image image) {
        Optional<UserImage> existingUserImage = userImageRepository.findByUserIdAndImageId(user.getId(), image.getId());

        return existingUserImage.orElseGet(() -> userImageRepository.save(new UserImage(user,image)));
    }

    public UserImage findByUserId(Long userId) {
        return userImageRepository.findByUserId(userId).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER_IMAGE));
    }

    public void deleteByUserId(Long userId) {
        userImageRepository.deleteByUserId(userId);
    }
}
