package com.example.community.facade;

import com.example.community.dto.response.ImageUrlResponse;
import com.example.community.s3.S3Service;
import com.example.community.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class S3ImageFacade {
    private final ImageService imageService;
    private final S3Service s3Service;

    S3ImageFacade(ImageService imageService, S3Service s3Service) {
        this.imageService = imageService;
        this.s3Service = s3Service;
    }

    @Transactional
    public ImageUrlResponse getUploadImageUrl() {
        Long imageId = imageService.makeImage();
        String uploadImageUrl =  s3Service.getCommunityImageUploadUrl(imageId);
        return new ImageUrlResponse(uploadImageUrl);
    }
}
