package com.example.community.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
