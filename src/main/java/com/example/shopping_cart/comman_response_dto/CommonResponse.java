package com.example.shopping_cart.comman_response_dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CommonResponse {
    boolean status;
    String message;
    Object data;
}
