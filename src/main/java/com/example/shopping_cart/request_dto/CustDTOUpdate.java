package com.example.shopping_cart.request_dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CustDTOUpdate {

    private Integer custId;
    private String custName;
    private String aadhaarNo;
    private String address;
}