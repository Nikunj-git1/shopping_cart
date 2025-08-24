package com.example.shopping_cart.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Entity
@Table (name = "customer")

@Data
@AllArgsConstructor
@NoArgsConstructor

@Builder

public class CustEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer custId;
    private String custName;
    private String aadhaarNo;
    private String address;
    private String pswd;
    private Date createdAt = new Date();
    private Date updatedAt;
}
