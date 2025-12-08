package com.example.community.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class BoardImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_image_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    Board board;

    @OneToOne
    @JoinColumn(name = "image_id")
    Image image;

    BoardImage() {}

    public BoardImage(Board board, Image image) {
        this.board = board;
        this.image = image;
    }
}
