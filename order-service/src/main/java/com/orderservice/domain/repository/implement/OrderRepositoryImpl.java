package com.orderservice.domain.repository.implement;

import com.orderservice.domain.common.OrderStatus;
import com.orderservice.domain.entity.Order;
import com.orderservice.domain.repository.OrderRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.orderservice.domain.common.OrderStatus.*;
import static com.orderservice.domain.entity.QOrder.order;


@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory query;

    public List<Order> findAfterRefund() {
        return query
                .selectFrom(order)
                .where(order.orderStatus.eq(REFUND_PROCESS))
                .fetch();
    }
}
