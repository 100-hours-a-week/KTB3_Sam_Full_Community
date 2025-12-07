package com.example.community.service;

import com.example.community.common.exception.BaseException;
import com.example.community.common.exception.ErrorCode;
import com.example.community.entity.Image;
import com.example.community.repository.ImageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageService imageService;

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
    void 입력받은_이미지_리스트에_해당하는_이미지_전체를_리스트로_반환한다() {
        //given
        List<Long> ids = List.of(1L, 2L, 3L);

        Image img1 = new Image();
        ReflectionTestUtils.setField(img1, "id", 1L);

        Image img2 = new Image();
        ReflectionTestUtils.setField(img2, "id", 2L);

        Image img3 = new Image();
        ReflectionTestUtils.setField(img3, "id", 3L);

        List<Image> mockedResult = List.of(img1, img2, img3);

        given(imageRepository.findByIds(ids)).willReturn(mockedResult);


        //when
        List<Image> result = imageService.findByIds(ids);


        //then
        assertThat(result).containsExactly(img1, img2, img3);
    }

    @Test
    void 이미지_아이디에_해당하는_이미지가_있을경우_성공적으로_이미지를_반환한다() {
        //given
        Long imageId = 1L;

        Image image = new Image();
        ReflectionTestUtils.setField(image, "id", imageId);

        given(imageRepository.findById(imageId)).willReturn(Optional.of(image));

        //when
        Image result = imageService.findById(imageId);

        //then
        assertThat(result.getId()).isEqualTo(image.getId());
    }

    @Test
    void 이미지_아이디에_해당하는_이미지가_없을경우_지정해둔_예외를_반환한다() {
        //given
        Long imageId = 1L;

        given(imageRepository.findById(imageId)).willReturn(Optional.empty());


        //when
        final BaseException exception = assertThrows(BaseException.class,
                () -> imageService.findById(imageId));


        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_IMAGE);

    }
}