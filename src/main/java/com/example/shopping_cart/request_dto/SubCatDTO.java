package com.example.shopping_cart.request_dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class SubCatDTO {

    @NotNull(message = "Category ID cannot be null")
    @Positive(message = "Category ID must be greater than 0")
    private Integer catId;

    @NotBlank(message = "Sub-category name cannot be blank")
    private String subCatName;

    @NotBlank(message = "Status cannot be blank")
    @Pattern(regexp = "^(Active|Inactive)$", message = "Status must be either Active or Inactive")
    private String status;
}