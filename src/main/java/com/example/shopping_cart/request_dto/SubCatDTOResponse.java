package com.example.shopping_cart.request_dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"subCatId", "catId", "subCatName", "status", "createdAt", "createdBy"})

public class SubCatDTOResponse {
    private Integer subCatId;
    private Integer catId;
    private String subCatName;
    private String status;
    private String createdAt;
    private Integer createdBy;
}