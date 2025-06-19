package com.example.shopping_cart.request_dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ItemDTOUpdate {

    @NotNull(message = "Item ID is required")
    @Positive(message = "Item ID must be a positive number")
    private Integer itemId;

    @NotNull(message = "Sub-category ID is required")
    @Positive(message = "Sub-category ID must be a positive number")
    private Integer subCatId;

    @NotBlank(message = "Item name is required")
    @Size(min = 1, max = 100, message = "Item name must be between 1 and 100 characters")
    private String itemName;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity must be 0 or greater")
    private Integer stockQty;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.1", inclusive = true, message = "Price must be at least 0.1")
    private Double price;

    @NotBlank(message = "Expiry date is required")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Expiry date must be in the format YYYY-MM-DD")
    private String expDate;

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "^(Active|Inactive)$", message = "Status must be either Active or Inactive")
    private String status;

    @Pattern(regexp = "^$|^(http(s?):)([/|.|\\w|\\s|-])*\\.(jpg|jpeg|png)$",
            message = "Photo must be a valid image URL (jpg, jpeg, png) or left empty")
    private String photo;
}