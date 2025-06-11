package com.example.shopping_cart.repository;

import com.example.shopping_cart.entity.SubCatEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubCatRepository extends JpaRepository<SubCatEntity, Integer> {

    List<SubCatEntity> findByStatus(String status);

    Optional<SubCatEntity> findBysubCatName(String subCatName);

    boolean existsBySubCatNameIgnoreCaseAndSubCatIdNot(String subCatName, Integer subCatId);

    List<SubCatEntity> findByCatId(int catId);
}