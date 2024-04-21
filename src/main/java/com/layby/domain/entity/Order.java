package com.layby.domain.entity;

import com.layby.domain.common.DeliveryStatus;
import com.layby.domain.common.OrderStatus;
import com.layby.web.exception.DeliveryCancelFailedException;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static com.layby.domain.common.ErrorCode.*;

@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
@Entity(name = "orders")
public class Order extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    //== 연관 관계 메서드 ==//
    public void mappingUserEntity(User user) {
        this.user = user;
        user.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.mappingOrderEntity(this);
    }

    public void mappingDeliveryEntity(Delivery delivery) {
        this.delivery = delivery;
        delivery.mappingOrderEntity(this);
    }

    //== 생성 메서드 ==//
    public static Order createOrder(
            User user, Delivery delivery, OrderItem... orderItem
    ) {
        Order order = new Order(null, OrderStatus.ORDER, user, null, delivery);

        for (OrderItem itemEntity : orderItem) {
            order.addOrderItem(itemEntity);
        }

        return order;
    }

    //== 비즈니스 로직 ==//
    /** 주문 취소 **/
    public void cancel() {
        DeliveryStatus deliveryStatus = delivery.getDeliveryStatus();
        if (deliveryStatus == DeliveryStatus.PROCESS ||
                deliveryStatus == DeliveryStatus.COMPLETE) {
            throw new DeliveryCancelFailedException(DELIVERY_ALEADY_START.getMessage());
        }

        this.orderStatus = OrderStatus.CANCEL;
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    //== 조회 로직 ==//
    /** 전체 주문 가격 조회 **/
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
