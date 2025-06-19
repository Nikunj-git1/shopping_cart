package com.example.shopping_cart.request_dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class OrderItemDTO {

    @NotNull(message = "Order item ID cannot be null")
    @Positive(message = "Order item ID must be a positive number")
    private Integer orderItemId;

    @NotNull(message = "Order ID cannot be null")
    @Positive(message = "Order ID must be a positive number")
    private Integer orderId;

    @NotNull(message = "Item ID cannot be null")
    @Positive(message = "Item ID must be a positive number")
    private Integer itemId;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer qty;

    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.1", inclusive = true, message = "Unit price must be at least 0.1")
    private Double unitPrice;
}