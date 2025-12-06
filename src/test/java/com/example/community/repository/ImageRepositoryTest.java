package com.example.community.repository;

import com.example.community.QueryDslTestConfig;
import com.example.community.entity.Image;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(QueryDslTestConfig.class)
class ImageRepositoryTest {
    @Autowired
    ImageRepository imageRepository;

    @Autowired
    EntityManager em;

    @Test
    void findByIds() {
        //given
        Image img1 = new Image();
        em.persist(img1);

        Image img2 = new Image();
        em.persist(img2);

        em.flush();
        em.clear();

        List<Long> imageIds = List.of(img1.getId(), img2.getId());


        //when
        List<Image> result = imageRepository.findByIds(imageIds);


        //then
        assertThat(result)
                .extracting("id")
                .containsExactlyInAnyOrder(img1.getId(), img2.getId());
    }
}