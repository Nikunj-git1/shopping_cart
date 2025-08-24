package com.example.shopping_cart.controller;

import com.example.shopping_cart.comman_response_dto.CommonResponse;
import com.example.shopping_cart.comman_response_dto.ResGenerator;
import com.example.shopping_cart.request_dto.OrderItemDTO;
import com.example.shopping_cart.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j

@RestController
@RequestMapping("/customer/order")
@Tag(name = "Order API", description = "For CRUD operation of orders.")

@Validated
public class OrderController {

    @Autowired
    OrderService orderService;


    @PostMapping("/create")
    public ResponseEntity<CommonResponse> create(@RequestBody @Valid List<OrderItemDTO> itemsList,
                                                 @AuthenticationPrincipal User user) {

        return ResGenerator.orderPlaced("Order placed successfully", orderService.create(itemsList, user));
    }


    @GetMapping("/get-order")
    public ResponseEntity<CommonResponse> getOrder(@RequestParam(required = true)
                                                   @NotNull(message = "Order ID must not be null")
                                                   @Positive(message = "Order ID must be a positive number")
                                                   Integer orderId) {

        return ResGenerator.success("Your order detail", orderService.getOrder(orderId));
    }


    @PatchMapping("/update-status/{orderId}/{status}")
    public ResponseEntity<CommonResponse> updateStatus(@PathVariable(required = true)
                                                       @NotNull(message = "Order ID must not be null")
                                                       @Positive(message = "Order ID must be a positive number")
                                                       Integer orderId,

                                                       @PathVariable(required = true)
                                                       @Pattern(
                                                               regexp = "^(PENDING|COMPLETED|CANCELLED)$",
                                                               message = "Status must be one of: PENDING, COMPLETED," +
                                                                       " CANCELLED"
                                                       )
                                                       String status,

                                                       @AuthenticationPrincipal User user) {

        return ResGenerator.success(
                "Status updated successfully", orderService.updateStatus(orderId, status, user));
    }
}