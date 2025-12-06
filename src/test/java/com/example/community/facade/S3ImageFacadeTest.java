package com.example.community.facade;

import com.example.community.dto.response.ImageUrlResponse;
import com.example.community.s3.S3Service;
import com.example.community.service.ImageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class S3ImageFacadeTest {
    @Mock
    ImageService imageService;

    @Mock
    S3Service s3Service;

    @InjectMocks
    S3ImageFacade s3ImageFacade;

    @Test
    void 이미지를_업로드할수있는_url을_반환받는다() {
        //given
        Long generatedImageId = 10L;
        String presignedUrl = "https://s3.com/upload/10";

        given(imageService.makeImage()).willReturn(generatedImageId);
        given(s3Service.getCommunityImageUploadUrl(generatedImageId))
                .willReturn(presignedUrl);


        //when
        ImageUrlResponse response = s3ImageFacade.getImageUploadUrl();


        //then
        assertThat(response.imageId()).isEqualTo(generatedImageId);
        assertThat(response.imagePresignedUrl()).isEqualTo(presignedUrl);
    }

    @Test
    void 이미지를_받을수있는_url을_반환받는다() {
        //given
        Long imageId = 5L;
        String presignedGetUrl = "https://s3.com/get/5";

        given(s3Service.getCommunityImage(imageId))
                .willReturn(presignedGetUrl);


        //when
        ImageUrlResponse response = s3ImageFacade.getImageUrl(imageId);


        //then
        assertThat(response.imageId()).isEqualTo(imageId);
        assertThat(response.imagePresignedUrl()).isEqualTo(presignedGetUrl);
    }
}