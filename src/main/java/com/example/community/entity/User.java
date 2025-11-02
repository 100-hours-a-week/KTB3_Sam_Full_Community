package com.example.community.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "user_id")
    private Long id;
    private String password;
    private String email;
    private String nickname;
    private Long profileImageId;

    public User(String email, String password, String nickname, Long profileImageId) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImageId = profileImageId;
    }

    public void updateUser(String nickname, Long profileImageId) {
        this.nickname = nickname;
        this.profileImageId = (profileImageId ==null) ? this.profileImageId : profileImageId;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
