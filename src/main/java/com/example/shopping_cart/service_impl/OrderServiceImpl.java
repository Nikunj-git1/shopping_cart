package com.example.shopping_cart.service_impl;

import com.example.shopping_cart.entity.OrderEntity;
import com.example.shopping_cart.entity.OrderItemEntity;
import com.example.shopping_cart.repository.OrderItemRepository;
import com.example.shopping_cart.repository.OrderRepository;
import com.example.shopping_cart.request_dto.OrderItemDTO;
import com.example.shopping_cart.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service

@Slf4j

public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    NamedParameterJdbcTemplate npJdbcTemplate;


    @Transactional
    public Integer create(List<OrderItemDTO> itemList, Integer custId) {

        OrderEntity order = new OrderEntity();

        order.setCustId(custId);

        orderRepository.save(order);

        List<OrderItemEntity> orderItemEntityList = new ArrayList<>();
        for (OrderItemDTO orderItemDTO : itemList) {

            OrderItemEntity item = new OrderItemEntity();

            item.setOrderId(order.getOrderId());
            item.setItemId(orderItemDTO.getItemId());
            item.setQty(orderItemDTO.getQty());
            item.setUnitPrice(orderItemDTO.getUnitPrice());

            orderItemEntityList.add(item);

        }

        orderItemRepository.saveAll(orderItemEntityList);


        return order.getOrderId();
    }

    public List<Map<String, Object>> getOrder(Integer orderId) {

        Optional<OrderEntity> isOrderId = orderRepository.findByOrderId(orderId);
        if (isOrderId.isEmpty()) {
        }

        String sql = """
                select oi.order_id, oi.order_item_id, oi.item_id, p.item_name from order_item oi
                left join customer_order co on co.order_id = oi.order_id
                left join item p on p.item_id = oi.item_id
                where oi.order_id = :orderId
                """;
        List<Map<String, Object>> result = npJdbcTemplate.queryForList(sql, Map.of("orderId", orderId));


        return result;
    }


    public OrderEntity updateStatus(Integer orderId, String status) {

        Optional<OrderEntity> optionalOrderEntity = orderRepository.findById(orderId);
        if (optionalOrderEntity.isEmpty()) {

        }

        OrderEntity orderEntity = optionalOrderEntity.get();
        orderEntity.setStatus(status);


        String dt = null;
        if (orderEntity.getOrderDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            dt = sdf.format(orderEntity.getOrderDate());

            Date formattedOrderDate = Date.valueOf(dt);
            orderEntity.setOrderDate(formattedOrderDate);
        }

        orderEntity = orderRepository.save(orderEntity);

        return orderEntity;

    }
}