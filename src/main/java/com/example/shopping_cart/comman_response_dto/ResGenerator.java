package com.example.shopping_cart.comman_response_dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResGenerator {
    public static ResponseEntity<CommonResponse> success(String message, Object data) {
        return new ResponseEntity<>(new CommonResponse(true, message, data), HttpStatus.OK);
    }

    public static ResponseEntity<CommonResponse> create(String message, Object data) {
        return new ResponseEntity<>(new CommonResponse(true, message, data), HttpStatus.CREATED);
    }

    public static ResponseEntity<CommonResponse> orderPlaced(String message, Integer orderId) {
        return new ResponseEntity<>(new CommonResponse(true, message, orderId), HttpStatus.CREATED);
    }
}