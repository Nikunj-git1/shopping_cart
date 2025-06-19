package com.example.shopping_cart.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Entity
@Table(name = "sub_category")

@Data
@AllArgsConstructor
@NoArgsConstructor

@Builder

@NamedQuery(name = "SubCatEntity.findByCategory",
        query = "select s from SubCatEntity s where s.catId = ?1")

public class SubCatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer subCatId;
    private Integer catId;
    private String subCatName;
    private String status;
    private Date createdAt = new Date();
    private Integer createdBy;
    private Date updatedAt;
    private Integer updatedBy;

}