package com.example.shopping_cart.request_dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ItemDTOUpdate {
    private Integer itemId;
    private Integer subCatId;
    private String itemName;
    private Integer stockQty;
    private Integer price;
    private String expDate;
    private String status;
    private String photo;
}