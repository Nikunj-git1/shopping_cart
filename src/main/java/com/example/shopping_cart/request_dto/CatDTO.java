package com.example.shopping_cart.request_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CatDTO {

    @NotBlank(message = "Category name is required")
    private String catName;

//        think witch way is best one line or double line for user info of criteria
//    @NotBlank(message = "Status must be either 'Active' or 'Inactive'")
    @NotBlank(message = "Status is required")
    @Pattern(regexp = "^(Active|Inactive)$", message = "Status must be either Active or Inactive")
    private String status;
}