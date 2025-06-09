package com.example.shopping_cart.request_dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SubCatDTOUpdate {

    private Integer subCatId;
    private Integer catId;
    private String subCatName;
    private String status;
}