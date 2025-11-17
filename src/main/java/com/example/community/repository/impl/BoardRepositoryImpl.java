package com.example.community.repository.impl;

import com.example.community.entity.*;
import com.example.community.repository.interfaces.BoardCustomRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class BoardRepositoryImpl implements BoardCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    BoardRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<Board> findAll(String title, String content, Pageable pageable) {
        QBoard board = QBoard.board;
        QUser user = QUser.user;
        QUserImage userImage = QUserImage.userImage;
        QImage image = QImage.image;

        BooleanBuilder builder = new BooleanBuilder();

        if(title != null && !title.isBlank()) {
            builder.and(board.title.containsIgnoreCase(title));
        }

        if(content != null && !content.isBlank()) {
            builder.and(board.content.containsIgnoreCase(content));
        }

        List<Board> results = jpaQueryFactory
                .select(board)
                .from(board)
                .join(board.user, user).fetchJoin()
                .join(user.userImage, userImage).fetchJoin()
                .join(userImage.image, image).fetchJoin()
                .where(builder)
                .orderBy(board.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = Optional.ofNullable(
                jpaQueryFactory
                        .select(board.count())
                        .from(board)
                        .where(builder)
                        .fetchOne())
                .orElse(0L);

        return new PageImpl<>(results, pageable, total);
    }
}
