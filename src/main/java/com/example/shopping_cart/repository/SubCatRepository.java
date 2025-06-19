package com.example.shopping_cart.repository;

import com.example.shopping_cart.entity.SubCatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubCatRepository extends JpaRepository<SubCatEntity, Integer> {

    List<SubCatEntity> findByStatus(String status);

    Optional<SubCatEntity> findBySubCatNameIgnoreCase(String subCatName);

    boolean existsBySubCatNameIgnoreCaseAndSubCatIdNot(String subCatName, Integer subCatId);

    List<SubCatEntity> findByCatId(Integer catId);

}