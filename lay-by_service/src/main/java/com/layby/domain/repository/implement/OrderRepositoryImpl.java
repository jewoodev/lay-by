package com.layby.domain.repository.implement;

import com.layby.domain.common.OrderStatus;
import com.layby.domain.entity.Order;
import com.layby.domain.entity.QDelivery;
import com.layby.domain.entity.QItem;
import com.layby.domain.entity.QOrder;
import com.layby.domain.repository.OrderRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.layby.domain.entity.QDelivery.*;
import static com.layby.domain.entity.QItem.*;
import static com.layby.domain.entity.QOrder.*;
import static com.layby.domain.entity.QOrderItem.orderItem;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<Order> findAllByUserIdWithOrderItemAndItem(Long userId) {
        return query
                .selectFrom(order)
                .join(order.orderItems, orderItem).fetchJoin()
                .join(orderItem.item, item).fetchJoin()
                .where(order.user.userId.eq(userId))
                .fetch();
    }

    @Override
    public List<Order> findAfterRefund() {
        return query
                .selectFrom(order)
                .join(order.orderItems, orderItem).fetchJoin()
                .join(orderItem.item, item).fetchJoin()
                .where(order.orderStatus.eq(OrderStatus.REFUND_PROCESS))
                .fetch();
    }

    @Override
    public Order findByOrderIdWithDelivery(Long orderId) {
        return query
                .selectFrom(order)
                .join(order.delivery, delivery).fetchJoin()
                .where(order.orderId.eq(orderId))
                .fetchOne();
    }
}
