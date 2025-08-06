package com.example.shopping_cart.repository;

import com.example.shopping_cart.entity.CatEntity;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CatRepository extends JpaRepository<CatEntity, Integer> {

    boolean existsByCatNameIgnoreCaseAndCatIdNot(String catName, Integer catId);

    List<CatEntity> findByStatus(String status);

    Optional<CatEntity> findByCatNameIgnoreCase(String catName);
}