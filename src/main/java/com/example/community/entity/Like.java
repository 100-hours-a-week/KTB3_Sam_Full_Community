package com.example.community.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Like extends BaseEntity{
    private Long id;
    private Long boardId;
    private Long userId;
}
