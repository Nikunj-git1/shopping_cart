package com.example.shopping_cart.request_dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"custId", "custName", "aadhaarNo", "address", "createdAt", "updatedAt"})

public class CustDTOResponse {

    private Integer custId;
    private String custName;
    private String aadhaarNo;
    private String address;
    private String createdAt;
    private String updatedAt;
}