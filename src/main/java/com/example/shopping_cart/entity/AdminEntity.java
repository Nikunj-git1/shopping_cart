package com.example.shopping_cart.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "admin")

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AdminEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer adminId;
    private String adminName;
    private String pswd;
    private String adminRole = "Admin";
    private Date createdAt = new Date();
}
