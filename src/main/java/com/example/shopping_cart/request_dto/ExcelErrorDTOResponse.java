package com.example.shopping_cart.request_dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelErrorDTOResponse {
    private int rowNumber;
    private String fieldName;
    private String message;
    private String found;
}