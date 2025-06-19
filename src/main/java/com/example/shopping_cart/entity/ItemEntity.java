package com.example.shopping_cart.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "item")

@Data
@AllArgsConstructor
@NoArgsConstructor

@Builder

public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer itemId;
    private Integer subCatId;
    private String itemName;
    private Integer stockQty;
    private Double price;
    private Date expDate;
    private String status;
    private String photo;
    private Date createdAt = new Date();
    private Integer createdBy;
    private Date updatedAt;
    private Integer updatedBy;
}