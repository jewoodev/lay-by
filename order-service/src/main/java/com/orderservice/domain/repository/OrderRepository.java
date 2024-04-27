package com.orderservice.domain.repository;

import com.orderservice.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

    Order findByOrderId(Long orderId);

    List<Order> findAllByUserId(Long userId);
}
