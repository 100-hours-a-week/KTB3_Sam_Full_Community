package com.example.community.entity;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BaseEntity {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    BaseEntity() {
        this.createdAt = java.time.LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void recordModificationTime() {
        this.updatedAt = LocalDateTime.now();
    }

    public void deleted() {this.deletedAt = LocalDateTime.now();}
}
