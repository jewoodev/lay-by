package com.layby.domain.repository;

import com.layby.domain.entity.Order;

import java.util.List;

public interface OrderRepositoryCustom {

    List<Order> findAllByUserIdWithOrderItemAndItem(Long userId);

    List<Order> findAfterRefund();

    Order findByOrderIdWithDelivery(Long orderId);
}
