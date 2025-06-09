package com.example.shopping_cart.service;

import com.example.shopping_cart.entity.OrderEntity;
import com.example.shopping_cart.request_dto.OrderItemDTO;

import java.util.List;
import java.util.Map;

public interface OrderService {

    Integer create(List<OrderItemDTO> itemsList, Integer custId);

    List<Map<String, Object>> getOrder(Integer orderId);

    OrderEntity updateStatus (Integer orderId, String status);
}