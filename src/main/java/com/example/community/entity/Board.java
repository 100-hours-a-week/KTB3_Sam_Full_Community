package com.example.community.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Board extends BaseEntity{
    private Long id;
    private String title;
    private String content;
    private List<Long> boardImageIds = new ArrayList<>();
    private Long userId;
}
