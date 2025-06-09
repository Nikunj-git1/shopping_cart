package com.example.shopping_cart.request_dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"orderId", "orderItemId", "itemId", "orderItemName", "qty", "unitPrice", "totalAmt"})


public class OrderItemDTOResponse {

    private Integer orderId;
    private Integer orderItemId;
    private Integer itemId;
    private String orderItemName;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalAmt;
}