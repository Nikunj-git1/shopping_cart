package com.example.shopping_cart.service_impl;

import com.example.shopping_cart.entity.OrderEntity;
import com.example.shopping_cart.entity.OrderItemEntity;
import com.example.shopping_cart.repository.OrderItemRepository;
import com.example.shopping_cart.repository.OrderRepository;
import com.example.shopping_cart.request_dto.OrderDTOResponse;
import com.example.shopping_cart.request_dto.OrderItemDTO;
import com.example.shopping_cart.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.example.shopping_cart.util.CustomizeDateFormat.formatTimestamp;

@Service


public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserLoginServiceImpl userLoginServiceImpl;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private NamedParameterJdbcTemplate npJdbcTemplate;


    @Override
    @Transactional
    @CachePut(value = "orderListCache", key = "#result")
    public Integer create(List<OrderItemDTO> itemList, User user) {
        Integer custId = userLoginServiceImpl.getCustId(user);

        OrderEntity order = new OrderEntity();
        order.setCustId(custId);
        orderRepository.save(order);

        Integer orderId = order.getOrderId();

        List<OrderItemEntity> orderItems = itemList.stream()
                .map(dto -> {
                    OrderItemEntity item = new OrderItemEntity();
                    item.setOrderId(orderId);
                    item.setItemId(dto.getItemId());
                    item.setQty(dto.getQty());
                    item.setUnitPrice(dto.getUnitPrice());
                    return item;
                })
                .collect(Collectors.toList());
        orderItemRepository.saveAll(orderItems);

        return order.getOrderId();
    }


    @Override
    @Cacheable(value = "orderListCache", key = "'ORDER_' + #orderId",
            unless = "#result == null || #result.isEmpty()")
    public List<Map<String, Object>> getOrder(Integer orderId) {
        orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoSuchElementException(
                        "This order id '" + orderId + "' not found."));

        String sql = """
                select oi.order_id, oi.order_item_id, oi.item_id, p.item_name from order_item oi
                left join customer_order co on co.order_id = oi.order_id
                left join item p on p.item_id = oi.item_id
                where oi.order_id = :orderId
                """;
        List<Map<String, Object>> result = npJdbcTemplate.queryForList(sql, Map.of("orderId", orderId));

        return result;
    }


    @Override
    @CacheEvict(value = "orderListCache", allEntries = true)
    public OrderDTOResponse updateStatus(Integer orderId, String status, User user) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("This order id '" + orderId + "' not found."));

        Integer adminId = userLoginServiceImpl.getAdminId(user);

        orderEntity.setStatus(status);
        orderEntity.setUpdatedAt(new java.util.Date());
        orderEntity.setUpdatedBy(adminId);
        orderEntity = orderRepository.save(orderEntity);

        String orderDate = formatTimestamp(orderEntity.getOrderDate());
        String updatedAt = formatTimestamp(orderEntity.getUpdatedAt());

        OrderDTOResponse orderDTOResponse = new OrderDTOResponse();

        orderDTOResponse.setOrderId(orderEntity.getOrderId());
        orderDTOResponse.setCustId(orderEntity.getCustId());
        orderDTOResponse.setOrderDate(orderDate);
        orderDTOResponse.setStatus(orderEntity.getStatus());
        orderDTOResponse.setUpdatedAt(updatedAt);
        orderDTOResponse.setUpdatedBy(orderEntity.getUpdatedBy());

        return orderDTOResponse;

    }
}