package com.example.shopping_cart.repository;

import com.example.shopping_cart.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface ItemRepository extends JpaRepository<ItemEntity, Integer> {

    boolean existsByItemNameIgnoreCaseAndItemIdNot(String ItemName, Integer ItemId);

    Optional<ItemEntity> findByItemName(String itemName);

}