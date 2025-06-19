package com.example.shopping_cart.request_dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

public class SubCatDTO {

    @NotNull(message = "Sub category ID cannot be null")
    @Positive(message = "Sub category ID must be a positive number")
    private Integer catId;

    @NotBlank(message = "Sub category name cannot be blank")
    private String subCatName;

    @NotBlank(message = "Status cannot be blank")
    @Pattern(regexp = "^(Active|Inactive)$", message = "Status must be either Active or Inactive")
    private String status;
}