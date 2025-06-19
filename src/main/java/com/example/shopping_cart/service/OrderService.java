package com.example.shopping_cart.service;

import com.example.shopping_cart.request_dto.OrderDTOResponse;
import com.example.shopping_cart.request_dto.OrderItemDTO;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Map;

public interface OrderService {

    Integer create(List<OrderItemDTO> itemsList, User user);

    List<Map<String, Object>> getOrder(Integer orderId);

    OrderDTOResponse updateStatus (Integer orderId, String status, User user);
}