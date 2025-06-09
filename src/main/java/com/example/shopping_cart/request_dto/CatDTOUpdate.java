package com.example.shopping_cart.request_dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CatDTOUpdate {

    @NotNull(message = "Category ID is required")
    @Positive(message = "Category ID must be a positive number")
    private Integer catId;
    private String catName;
    private String status;
}