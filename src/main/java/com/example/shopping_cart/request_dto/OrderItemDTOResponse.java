package com.example.shopping_cart.request_dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"orderId", "orderItemId", "itemId", "qty", "unitPrice", "totalAmt"})


public class OrderItemDTOResponse {

    private Integer orderId;
    private Integer orderItemId;
    private Integer itemId;
    private Integer qty;
    private Double unitPrice;
    private Integer totalAmt;
}