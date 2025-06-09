package com.example.shopping_cart.repository;

import com.example.shopping_cart.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface AdminRepository extends JpaRepository<AdminEntity, Integer> {

    Optional<AdminEntity> findByAdminName (String adminName);
}