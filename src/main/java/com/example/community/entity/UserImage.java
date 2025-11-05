package com.example.community.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class UserImage extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    User user;

    @OneToOne
    @JoinColumn(name = "image_id")
    Image image;
}
