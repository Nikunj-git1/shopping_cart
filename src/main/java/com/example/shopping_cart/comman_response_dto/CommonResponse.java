package com.example.shopping_cart.comman_response_dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CommonResponse {
    boolean status;
    String message;
    Object data;
}
