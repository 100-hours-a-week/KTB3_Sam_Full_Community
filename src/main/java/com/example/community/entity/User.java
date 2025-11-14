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

    @OneToOne(mappedBy = "user")
    private UserImage userImage;

    User() {}

    public User(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public void updateUser(String nickname) {
        this.nickname = nickname;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    @Override
    public void setId(Long id) {this.id = id;}
}
