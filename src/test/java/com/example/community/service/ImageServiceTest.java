package com.example.community.service;

import com.example.community.entity.Image;
import com.example.community.repository.ImageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @Mock
    ImageRepository imageRepository;

    @InjectMocks
    ImageService imageService;

    @Test
    void 이미지가_성공적으로_생성된다() {
        //given
        Image savedImage = new Image();
        ReflectionTestUtils.setField(savedImage, "id", 10L);

        given(imageRepository.save(any(Image.class))).willReturn(savedImage);


        //when
        Long result = imageService.makeImage();


        //then
        assertThat(result).isEqualTo(10L);
    }

    @Test
    void findByIds() {
    }

    @Test
    void findById() {
    }
}