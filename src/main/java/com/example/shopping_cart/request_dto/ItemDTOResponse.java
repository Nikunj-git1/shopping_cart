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
@JsonPropertyOrder({"categoryName", "subCategoryName", "itemId", "itemName", "subCatId", "stockQty", "price",
        "expDate", "status", "photo", "createdAt", "createdBy"})

public class ItemDTOResponse {
    private String catName;
    private String subCatName;
    private Integer itemId;
    private String itemName;
    private Integer subCatId;
    private Integer stockQty;
    private Integer price;
    private String expDate;
    private String status;
    private String photo;
    private String createdAt;
    private Integer createdBy;
}