package com.example.community.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User extends BaseEntity{
    private Long id;
    private String password;
    private String email;
    private String nickname;
    private int profileImageId;

    public User(String email, String password, String nickname, int profileImageId) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImageId = profileImageId;
    }
}
