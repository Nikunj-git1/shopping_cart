package com.example.shopping_cart.entity;

import jakarta.persistence.*;
import lombok.*;

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
  @Column(name = "created_at", insertable = false, updatable = false)
    private Date createdAt;
}
