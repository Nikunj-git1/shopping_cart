package com.example.shopping_cart.repository;

import com.example.shopping_cart.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface OrderItemRepository extends JpaRepository <OrderItemEntity, Integer> {

}