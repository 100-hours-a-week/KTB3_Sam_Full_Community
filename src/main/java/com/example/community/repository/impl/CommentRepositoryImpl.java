package com.example.community.repository.impl;

import com.example.community.entity.Comment;
import com.example.community.entity.QBoard;
import com.example.community.entity.QComment;
import com.example.community.entity.QUser;
import com.example.community.repository.interfaces.CommentCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CommentRepositoryImpl implements CommentCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    CommentRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<Comment> findAllByBoardId(Long boardId, Pageable pageable) {
        QComment comment = QComment.comment;
        QUser user = QUser.user;
        QBoard board = QBoard.board;

        List<Comment> comments = jpaQueryFactory
                .selectFrom(comment)
                .join(comment.user, user).fetchJoin()
                .join(comment.board, board).fetchJoin()
                .where(comment.board.id.eq(boardId))
                .orderBy(comment.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = Optional.ofNullable(
                jpaQueryFactory
                        .select(comment.count())
                        .from(comment)
                        .where(comment.board.id.eq(boardId))
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(comments, pageable, total);
    }
}
