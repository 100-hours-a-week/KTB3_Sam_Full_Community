package com.example.community.repository;

import com.example.community.entity.Like;
import com.example.community.repository.interfaces.LikeCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long>, LikeCustomRepository {
    @Query("select l from Like l join fetch l.user join fetch l.board where l.user.id = :userId and l.board.id = :boardId")
    Optional<Like> findByUserIdAndBoardId(Long userId, Long boardId);

    @Query("select l from Like l join fetch l.board where l.board.id in :boardIds")
    List<Like> findAllByBoardIds(@Param("boardIds") List<Long> boardIds);

    @Query("select l from Like l join fetch l.board where l.board.id = :boardId")
    List<Like> findAllByBoardId(Long boardId);

    @Modifying
    @Query("delete from Like l where l.board.id = :boardId")
    void deleteByBoardId(@Param("boardId") Long boardId);
}
