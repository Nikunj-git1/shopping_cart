package com.example.shopping_cart.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_item")

@Data
@AllArgsConstructor
@NoArgsConstructor


public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer orderItemId;
    private Integer orderId;
    private Integer itemId;
    private Integer qty;
    private Double unitPrice;
    @Column(name = "total_amt", insertable = false, updatable = false)
    private Integer totalAmt;
}