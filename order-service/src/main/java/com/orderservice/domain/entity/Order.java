package com.orderservice.domain.entity;

import com.orderservice.domain.common.DeliveryStatus;
import com.orderservice.domain.common.OrderStatus;
import com.orderservice.web.exception.DeliveryCancelFailedException;
import com.orderservice.web.exception.RefundFailedException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

import static com.orderservice.domain.common.DeliveryStatus.*;
import static com.orderservice.domain.common.ErrorCode.DELIVERY_ALEADY_START;
import static com.orderservice.domain.common.ErrorCode.REFUND_IS_NOT_POSSIBLE;
import static com.orderservice.domain.common.OrderStatus.*;
import static com.orderservice.domain.common.OrderStatus.CANCEL;

@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
@Entity(name = "orders")
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "total_price")
    private int totalPrice;

    private LocalDateTime refundRequestDate;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "delivery_id")
    private Long deliveryId;

    //== 연관 관계 메서드 ==//
    public void mappingUserId(Long userId) {
        this.userId = userId;
    }

    //== 생성 메서드 ==//
    public static Order createOrder(Long userId, Long deliveryId) {
        return Order.builder()
                .orderStatus(ORDER)
                .userId(userId)
                .deliveryId(deliveryId)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();
    }


    //== 비즈니스 로직 ==//
    /** 상태 업데이트 **/
    public void updateStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    /** 주문 취소 **/
    public void cancel(Delivery delivery) {
        DeliveryStatus deliveryStatus = delivery.checkStatus();
        if (deliveryStatus == PROCESS || deliveryStatus == COMPLETE) {
            throw new DeliveryCancelFailedException(DELIVERY_ALEADY_START.getMessage());
        }

        updateStatus(CANCEL);
    }

    /** 환불 **/
    public void refund(Delivery delivery) {
        long pastDay = delivery.checkPastDay();
        if (pastDay <= 3 && pastDay >= 2) {
            updateStatus(REFUND_PROCESS);
            this.refundRequestDate = LocalDateTime.now();
        }
        else throw new RefundFailedException(REFUND_IS_NOT_POSSIBLE.getMessage());
    }

    /** 총 비용을 매핑하는 메서드 **/
    public void mappingTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
