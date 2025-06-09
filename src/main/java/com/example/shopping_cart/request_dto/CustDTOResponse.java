package com.example.shopping_cart.request_dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"custId", "custName", "aadhaarNo", "address", "createdAt"})

public class CustDTOResponse {

    private Integer custId;
    private String custName;
    private String aadhaarNo;
    private String address;
    private String createdAt;
}
