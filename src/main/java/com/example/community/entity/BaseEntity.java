package com.example.community.entity;

import java.time.LocalDateTime;

public class BaseEntity {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    BaseEntity() {
        this.createdAt = java.time.LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void recordModificationTime() {
        this.updatedAt = LocalDateTime.now();
    }
}
