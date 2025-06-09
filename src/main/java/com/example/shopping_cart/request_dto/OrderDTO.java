//package com.example.shopping_cart.request_dto;
//
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Pattern;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//
//public class OrderDTO {
//
//    @NotNull(message = "Order id is required")
//    private Integer orderId;
//
//    @Pattern(regexp = "^(PENDING|PROCESSING|SHIPPED|COMPLETED|CANCELLED)$",
//            message = "Please select a valid status: PENDING, PROCESSING, SHIPPED, COMPLETED, or CANCELLED.")
//    private String status;
//}
