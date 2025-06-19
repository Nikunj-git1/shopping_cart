package com.example.shopping_cart.comman_response_dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor

public class CommonException {
    private boolean status;
    private String message;
    private Object data;

    @Override
    public String toString() {

        return "CommonException{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}