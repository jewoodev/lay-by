package com.orderservice.web.util;

import com.orderservice.domain.dto.ItemStockDto;
import com.orderservice.domain.dto.ItemStockDtoList;
import com.orderservice.domain.entity.Delivery;
import com.orderservice.domain.entity.Order;
import com.orderservice.domain.entity.OrderItem;
import com.orderservice.web.client.ItemServiceClient;
import com.orderservice.web.service.DeliveryService;
import com.orderservice.web.service.OrderItemService;
import com.orderservice.web.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.orderservice.domain.common.DeliveryStatus.RETURN_COMPLETE;
import static com.orderservice.domain.common.OrderStatus.REFUND_COMPLETE;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {

    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final DeliveryService deliveryService;
    private final ItemServiceClient itemServiceClient;

    // 반품 후 재고 처리를 하는 로직
    @Scheduled(fixedRate = 10000)
    @Transactional
    public void inventoryProcessAfterRefund() throws InterruptedException {

        // 반품 신청 상태인 오더들을 조회해온 후
        List<Order> orders = orderService.findAfterRefund();

        // 만약 없으면 여기서 로직 종료
        if (orders.isEmpty()) return;

        // 현재 시간으로 부터
        LocalDateTime now = LocalDateTime.now();

        for (Order order : orders) {
            // 반품 신청한 날짜의 간격이 1일 이상이면
            LocalDateTime refundRequestDate = order.getRefundRequestDate();
            long pastDay = ChronoUnit.DAYS.between(refundRequestDate, now);

            if (pastDay >= 1) {
                // 반품 완료로 상태를 바꾸고 재고를 채운다.
                order.updateStatus(REFUND_COMPLETE);
                List<OrderItem> orderItems = orderItemService.findAllByOrderId(order.getOrderId());
                List<ItemStockDto> itemStockDtos = new ArrayList<>();
                for (OrderItem orderItem : orderItems) {
                    itemStockDtos.add(new ItemStockDto(orderItem.getOrderId(), orderItem.getCount()));
                }
                ItemStockDtoList itemStockDtoList = new ItemStockDtoList(itemStockDtos);
                itemServiceClient.increaseStock(itemStockDtoList);

                // 배송 상태도 반품 완료로 업데이트한다.
                Delivery delivery = deliveryService.findByOrderId(order.getOrderId());
                delivery.updateStatus(RETURN_COMPLETE);
            }
        }
    }
}
