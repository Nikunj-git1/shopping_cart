package com.example.shopping_cart.request_dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ItemDTO {

    @NotNull(message = "Sub-category ID is required")
    @Positive(message = "Sub-category ID must be a positive number")
    private Integer subCatId;

    @NotBlank(message = "Item name is required")
    @Size(min = 1, max = 100, message = "Item name must be between 1 and 100 characters")
    private String itemName;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.1", inclusive = true, message = "Price must be at least 0.1")
    private Double price;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity must be 0 or greater")
    private Integer stockQty;

    @NotBlank(message = "Expiry date is required")
    @Pattern(regexp = "^([0][1-9]|[1-2][0-9]|3[0-1])-(0[1-9]|1[0-2])-(\\d{4})$", message = "Expiry date must be in dd-MM-yyyy format")
    private String expDate;


    @NotBlank(message = "Status is required")
    @Pattern(regexp = "^(Active|Inactive)$", message = "Status must be either Active or Inactive")
    private String status;

    @NotNull(message = "Photo is required")
    private MultipartFile photo;
}