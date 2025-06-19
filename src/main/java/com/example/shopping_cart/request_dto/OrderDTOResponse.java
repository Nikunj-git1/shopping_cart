package com.example.shopping_cart.request_dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class OrderDTOResponse {

    private Integer orderId;
    private Integer custId;
    private String orderDate;
    private String status;
    private String updatedAt;
    private Integer updatedBy;
}