package com.example.shopping_cart.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "category")

@Data
@AllArgsConstructor
@NoArgsConstructor

@Builder

public class CatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer catId;
    private String catName;
    private String status;
    private Date createdAt = new Date();
    private Integer createdBy;
    private Date updatedAt;
    private Integer updatedBy;
}