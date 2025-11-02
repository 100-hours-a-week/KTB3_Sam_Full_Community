package com.example.community.entity;

import com.example.community.entity.interfaces.Identifiable;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class User extends BaseEntity implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "user_id")
    private Long id;
    private String password;
    private String email;
    private String nickname;
    private Long profileImageId;

    @OneToMany(mappedBy = "author")
    private List<Board> posts = new ArrayList<>();

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

    public void addPost(Board post) {
        this.posts.add(post);
        post.setAuthor(this);
    }

    public void removePost(Board post) {
        this.posts.remove(post);
        post.setAuthor(null);
    }
}
