package com.example.community.entity;

import com.example.community.entity.interfaces.Identifiable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class Image extends BaseEntity implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "image_id")
    private Long id;

    @Override
    public void setId(Long id) {this.id = id;}
}
