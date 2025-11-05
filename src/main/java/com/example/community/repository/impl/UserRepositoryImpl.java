package com.example.community.repository.impl;

import com.example.community.entity.QUser;
import com.example.community.entity.User;
import com.example.community.repository.interfaces.UserCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    UserRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        QUser user = QUser.user;

        return Optional.ofNullable(jpaQueryFactory
                .select(user)
                .from(user)
                .where(user.email.eq(email))
                .fetchOne());
    }

    @Override
    public Optional<User> findByNickname(String nickname) {
        QUser user = QUser.user;

        return Optional.ofNullable(jpaQueryFactory
                .select(user)
                .from(user)
                .where(user.nickname.eq(nickname))
                .fetchOne());
    }
}
