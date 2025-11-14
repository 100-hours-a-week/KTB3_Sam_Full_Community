package com.example.community.repository;

import com.example.community.entity.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {
    @Query("select b from BoardImage b join fetch b.board join fetch b.image where b.board.id = :boardId and b.image.id = :imageId")
    Optional<BoardImage> findByBoardIdAndImageId(@Param("boardId") Long boardId, @Param("imageId") Long imageId);
}
