package com.orderservice.domain.repository;

import com.orderservice.domain.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    OrderItem findByOrderItemId(Long orderItemId);

    List<OrderItem> findAllByOrderId(Long orderId);
}
