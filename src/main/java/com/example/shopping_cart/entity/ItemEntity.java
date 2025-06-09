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
    private Integer price;
    private Date expDate;
    private String status;
    private String photo;
    @Column(name = "created_at", insertable = false, updatable = false)
    private Date createdAt;
    private Integer createdBy;
}