package com.layby.domain.repository;

import com.layby.domain.entity.Order;
import com.layby.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findByOrderId(Long orderId);

    List<Order> findAllByUser(User user);
}
