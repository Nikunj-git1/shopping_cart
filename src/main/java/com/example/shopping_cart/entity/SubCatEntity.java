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
    private int catId;
    private String subCatName;
    private String status;
    @Column(name = "created_at", insertable = false, updatable = false)
    private Date createdAt;
    private Integer createdBy;

}