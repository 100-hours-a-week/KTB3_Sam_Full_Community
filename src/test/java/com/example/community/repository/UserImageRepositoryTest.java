package com.example.community.repository;

import com.example.community.QueryDslTestConfig;
import com.example.community.entity.Image;
import com.example.community.entity.User;
import com.example.community.entity.UserImage;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QueryDslTestConfig.class)
class UserImageRepositoryTest {

    @Autowired
    private UserImageRepository userImageRepository;

    @Autowired
    private EntityManager em;

    @Test
    void findByUserId() {
        // given
        User user = new User("email", "password", "nickname");
        Image image = new Image();

        em.persist(user);
        em.persist(image);

        UserImage userImage = new UserImage(user, image);
        em.persist(userImage);

        em.flush();
        em.clear();


        // when
        Optional<UserImage> result = userImageRepository.findByUserId(user.getId());

        // then
        assertThat(result).isPresent();
        UserImage found = result.get();

        assertThat(found.getUser().getClass().getName())
                .doesNotContain("HibernateProxy");

        assertThat(found.getImage().getClass().getName())
                .doesNotContain("HibernateProxy");
    }

    @Test
    void deleteByUserId() {
        // given
        User user = new User("email", "password", "nickname");
        Image image = new Image();

        em.persist(user);
        em.persist(image);

        UserImage userImage = new UserImage(user, image);
        em.persist(userImage);

        em.flush();
        em.clear();


        // when
        userImageRepository.deleteByUserId(user.getId());
        em.flush();


        // then
        Optional<UserImage> result = userImageRepository.findByUserId(user.getId());
        assertThat(result).isEmpty();
    }
}