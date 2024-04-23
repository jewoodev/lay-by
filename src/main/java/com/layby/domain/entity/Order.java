package com.layby.domain.entity;

import com.layby.domain.common.DeliveryStatus;
import com.layby.domain.common.ErrorCode;
import com.layby.domain.common.OrderStatus;
import com.layby.domain.dto.response.OrderStatusResponseDto;
import com.layby.web.exception.DeliveryCancelFailedException;
import com.layby.web.exception.RefundFailedException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDate refundRequestDate;

    //== 연관 관계 메서드 ==//
    public void mappingUser(User user) {
        this.user = user;
        user.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.mappingOrder(this);
    }

    public void mappingDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.mappingOrder(this);
    }

    //== 생성 메서드 ==//
    public static Order createOrder(
            User user, Delivery delivery, List<OrderItem> orderItems
    ) {
        Order order = Order.builder()
                .orderStatus(OrderStatus.ORDER)
                .user(user)
                .orderItems(new ArrayList<>())
                .delivery(delivery)
                .build();

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        return order;
    }

    //== 변환 메서드 ==//
    public static OrderStatusResponseDto convertToStatusDto(Order order) {

        // 배송 정보는 checkStatus() 로 업데이트해서 response
        OrderStatusResponseDto orderStatusResponseDto = OrderStatusResponseDto.builder()
                .orderStatus(order.getOrderStatus().getDescription())
                .deliveryStatus(order.getDelivery().checkStatus().getDescription())
                .totalPrice(order.getTotalPrice())
                .build();

        return orderStatusResponseDto;
    }

    //== 비즈니스 로직 ==//
    /** 주문 취소 **/
    public void cancel() {
        DeliveryStatus deliveryStatus = delivery.checkStatus();
        if (deliveryStatus == DeliveryStatus.PROCESS ||
                deliveryStatus == DeliveryStatus.COMPLETE) {
            throw new DeliveryCancelFailedException(DELIVERY_ALEADY_START.getMessage());
        }

        this.orderStatus = OrderStatus.CANCEL;
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    /** 환불 **/
    public void refund() {
        long pastDay = delivery.checkPastDay();
        if (pastDay == 3) {
            this.orderStatus = OrderStatus.REFUND_PROCESS;
            this.refundRequestDate = LocalDate.now();
        }
        else throw new RefundFailedException(REFUND_IS_NOT_POSSIBLE.getMessage());
    }

    /** 상태 업데이트 **/
    public void updateStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
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
