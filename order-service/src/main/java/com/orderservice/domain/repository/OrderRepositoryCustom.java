package com.orderservice.domain.repository;


import com.orderservice.domain.entity.Order;

import java.util.List;

public interface OrderRepositoryCustom {

    List<Order> findAfterRefund();
}
