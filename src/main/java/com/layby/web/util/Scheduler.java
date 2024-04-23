package com.layby.web.util;

import com.layby.domain.common.OrderStatus;
import com.layby.domain.entity.Order;
import com.layby.domain.entity.OrderItem;
import com.layby.web.service.OrderService;
import com.layby.web.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {

    private final OrderService orderService;

    // 반품 후 재고 처리를 하는 로직
//    @Scheduled(fixedRate = 10000)
//    @Transactional
//    public void inventoryProcessAfterRefund() throws InterruptedException {
//
//        // 반품 신청 상태인 오더들을 조회해온 후
//        List<Order> orders = orderService.findAtferRefund();
//
//        // 만약 없으면 여기서 로직 종료
//        if (orders.isEmpty()) return;
//
//        // 현재 시간으로 부터
//        LocalDate now = LocalDate.now();
//
//        for (Order order : orders) {
//            // 반품 신청한 날짜의 간격이 1일 이상이면
//            LocalDate refundRequestDate = order.getRefundRequestDate();
//            Period period = Period.between(refundRequestDate, now);
//
//            if (period.getDays() >= 1) {
//                // 반품 완료로 상태를 바꾸고 재고를 채운다.
//                order.updateStatus(OrderStatus.REFUND_COMPLETE);
//                List<OrderItem> orderItems = order.getOrderItems();
//                for (OrderItem orderItem : orderItems) {
//                    orderItem.cancel();
//                }
//            }
//        }
//    }
}
