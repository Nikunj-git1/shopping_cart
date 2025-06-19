package com.example.shopping_cart.request_dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"categoryName", "subCategoryName", "subCatId", "itemId", "itemName", "stockQty", "price",
        "expDate", "status", "photo", "createdAt", "createdBy", "updatedAt", "updatedBy"})

public class ItemDTOResponse {
    private String catName;
    private String subCatName;
    private Integer subCatId;
    private Integer itemId;
    private String itemName;
    private Integer stockQty;
    private Double price;
    private String expDate;
    private String status;
    private String photo;
    private String createdAt;
    private Integer createdBy;
    private String updatedAt;
    private Integer updatedBy;
}