package com.example.community.entity;

import java.util.ArrayList;
import java.util.List;

public class Board extends BaseEntity{
    private Long id;
    private String title;
    private String content;
    private List<Long> boardImageIds = new ArrayList<>();
    private Long userId;
}
