package com.example.shopping_cart.request_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SubCatDTOUpdate {

    @NotNull(message = "Sub category ID is required")
    @Positive(message = "Sub category ID must be a positive number")
    private Integer subCatId;

    @NotNull(message = "Category ID cannot be null")
    @Positive(message = "Category ID must be a positive number")
    private Integer catId;

    @NotBlank(message = "Sub category name cannot be blank")
    private String subCatName;

    @NotBlank(message = "Status cannot be blank")
    @Pattern(regexp = "^(Active|Inactive)$", message = "Status must be either Active or Inactive")
    private String status;
}