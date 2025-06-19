package com.example.shopping_cart.request_dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"subCatId", "catId", "subCatName", "status", "createdAt", "createdBy", "updatedAt", "updatedBy"})

public class SubCatDTOResponse {

    private Integer subCatId;
    private Integer catId;
    private String subCatName;
    private String status;
    private String createdAt;
    private Integer createdBy;
    private String updatedAt;
    private Integer updatedBy;
}