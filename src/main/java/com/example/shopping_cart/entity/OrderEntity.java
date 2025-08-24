package com.example.shopping_cart.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "customer_order")

@Data
@AllArgsConstructor
@NoArgsConstructor

public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;
    private Integer custId;
    private Date orderDate = new Date();
    private String status = "Pending";
    private Date updatedAt;
    private Integer updatedBy;
}