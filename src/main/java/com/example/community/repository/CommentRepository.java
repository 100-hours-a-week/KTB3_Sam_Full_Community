package com.example.community.repository;

import com.example.community.entity.Board;
import com.example.community.entity.Comment;
import com.example.community.repository.interfaces.CommentCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentCustomRepository {
    @Query("select c from Comment c join fetch c.user join fetch c.board where c.board.id in :boardIds ")
    List<Comment> findAllByBoardId(List<Long> boardIds);

    @Query("select c from Comment c join fetch c.user u join fetch u.userImage ui join fetch c.board b where b.id= :boardId")
    List<Comment> findAllByBoardId(Long boardId);

    @Modifying
    @Query("delete from Comment c where c.board.id = :boardId")
    void deleteByBoardId(Long boardId);

    @Query("select c from Comment c join fetch c.user u join fetch u.userImage ui join fetch ui.image i join fetch c.board b where b.id= :boardId")
    Page<Comment> findAllByBoardId(@Param("boardId") Long boardId, Pageable pageable);
}
