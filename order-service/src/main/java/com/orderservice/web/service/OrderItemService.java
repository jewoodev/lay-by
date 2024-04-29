package com.orderservice.web.service;


import com.orderservice.domain.entity.OrderItem;

import java.util.List;

public interface OrderItemService {

    void save(OrderItem orderItem);

    OrderItem findByOrderItemId(Long orderItemId);

    List<OrderItem> findAllByOrderId(Long orderId);
}
