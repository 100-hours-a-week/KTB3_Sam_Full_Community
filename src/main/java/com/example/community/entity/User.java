package com.example.community.entity;

import com.example.community.entity.interfaces.Identifiable;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "users")
public class User extends BaseEntity implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "user_id")
    private Long id;
    private String password;
    private String email;
    private String nickname;
    private Long profileImageId;

    User() {}

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

    @Override
    public void setId(Long id) {this.id = id;}
}
