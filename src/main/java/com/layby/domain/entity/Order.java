package com.layby.domain.entity;

import com.layby.domain.common.DeliveryStatus;
import com.layby.domain.common.OrderStatus;
import com.layby.domain.dto.response.OrderResponseDto;
import com.layby.web.exception.DeliveryCancelFailedException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    public void mappingUser(User user) {
        this.user = user;
        user.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.mappingOrder(this);
    }

    public void mappingDeliveryEntity(Delivery delivery) {
        this.delivery = delivery;
        delivery.mappingOrderEntity(this);
    }

    //== 생성 메서드 ==//
    public static Order createOrder(
            User user, Delivery delivery, List<OrderItem> orderItems
    ) {
        Order order = new Order(null, OrderStatus.ORDER, user, null, delivery);

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        return order;
    }

    //== 변환 메서드 ==//
    public static OrderResponseDto convertToDto(Order order) {
        OrderResponseDto orderResponseDto = OrderResponseDto.builder()
                .orderStatus(order.getOrderStatus().getDescription())
                .deliveryStatus(order.getDelivery().getDeliveryStatus().getDescription())
                .build();

        return orderResponseDto;
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

    /** 배송 상태 업데이트 **/
    public void updateOrderStatus() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime orderedDate = getCreatedDate();

        long passDays = ChronoUnit.DAYS.between(now, orderedDate);
        if (passDays == 1) this.delivery.updateStatus(DeliveryStatus.PROCESS);
        else if (passDays > 1) this.delivery.updateStatus(DeliveryStatus.COMPLETE);
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
