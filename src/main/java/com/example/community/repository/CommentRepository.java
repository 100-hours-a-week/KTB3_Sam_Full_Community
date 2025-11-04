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
    @Query("select c from comment c join fetch c.user join fetch c.board where c.board.id in :boardIds ")
    public List<Comment> findAllByBoardId(List<Long> boardIds);

    @Query("select c from Comment c join fetch c.user join fetch c.board where c.board.id= :boardId")
    public List<Comment> findAllByBoardId(Long boardId);

    @Modifying
    @Query("delete c from Comment c join c.board b where b.id = :boardId")
    public void deleteByBoardId(Long boardId);

    @Query("select c from Comment c join fetch c.user join fetch c.board where c.board.id= :boardId")
    public Page<Comment> findAllByBoardId(@Param("boardId") Long boardId, Pageable pageable);
}
