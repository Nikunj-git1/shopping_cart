package com.example.shopping_cart.repository;

import com.example.shopping_cart.entity.OrderEntity;
import com.example.shopping_cart.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

    Optional<OrderEntity> findByOrderId (Integer orderId);

}