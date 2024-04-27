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

import com.orderservice.domain.dto.OrderStatusDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.orderservice.domain.common.ErrorCode.DELIVERY_ALEADY_START;
import static com.orderservice.domain.common.ErrorCode.REFUND_IS_NOT_POSSIBLE;

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

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "delivery_id")
    private Long deliveryId;

    private LocalDateTime refundRequestDate;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    //== 연관 관계 메서드 ==//
    public void mappingUserId(Long userId) {
        this.userId = userId;
    }


    //== 생성 메서드 ==//
    public static Order createOrder(Long userId, Long deliveryId) {
        return Order.builder()
                .orderStatus(OrderStatus.ORDER)
                .userId(userId)
                .deliveryId(deliveryId)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();
    }


    //== 비즈니스 로직 ==//
    /** 주문 취소 **/
    public void cancel(Delivery delivery) {
        DeliveryStatus deliveryStatus = delivery.checkStatus();
        if (deliveryStatus == DeliveryStatus.PROCESS ||
                deliveryStatus == DeliveryStatus.COMPLETE) {
            throw new DeliveryCancelFailedException(DELIVERY_ALEADY_START.getMessage());
        }

        this.orderStatus = OrderStatus.CANCEL;
    }

    /** 환불 **/
    public void refund(Delivery delivery) {
        long pastDay = delivery.checkPastDay();
        if (pastDay <= 3 && pastDay >= 2) {
            this.orderStatus = OrderStatus.REFUND_PROCESS;
            this.refundRequestDate = LocalDateTime.now();
        }
        else throw new RefundFailedException(REFUND_IS_NOT_POSSIBLE.getMessage());
    }

    /** 상태 업데이트 **/
    public void updateStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void mappingTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}