package com.orderservice.web.service.implement;

import com.orderservice.domain.dto.*;
import com.orderservice.domain.entity.Delivery;
import com.orderservice.domain.entity.Order;
import com.orderservice.domain.entity.OrderItem;
import com.orderservice.domain.vo.ItemStockRequest;
import com.orderservice.web.client.ItemServiceClient;
import com.orderservice.domain.repository.OrderRepository;
import com.orderservice.web.service.DeliveryService;
import com.orderservice.web.service.OrderItemService;
import com.orderservice.web.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;
    private final DeliveryService deliveryService;
    private final ItemServiceClient itemServiceClient;

    @Override
    public Long save(Order order) {
        Order saved = orderRepository.save(order);
        return saved.getOrderId();
    }

    @Override
    public Order findByOrderId(Long orderId) {
        return orderRepository.findByOrderId(orderId);
    }

    @Override
    public ResponseEntity<List<OrderStatusDto>> referOrdersStatus(Long userId) {

        // 현재 인증되어진 유저의 전체 주문 목록을 조회해서
        List<Order> allByUser = orderRepository.findAllByUserId(userId);
        List<OrderStatusDto> responseBody = new ArrayList<>();

        // Dto로 변환해서
        for (Order order : allByUser) {
            Delivery delivery = deliveryService.findByDeliveryId(order.getDeliveryId());
            OrderStatusDto orderStatusDto = OrderStatusDto.convertToStatusDto(order, delivery.getDeliveryStatus());
            responseBody.add(orderStatusDto);
        }

        // 반환한다.
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    /**
     주문 취소 메서드.
     1. 주문 취소가 가능한지 order에 지정된 delivery status를 확인해 판단하고 가능하면
     delivery와 order의 상태를 취소로 바꾼 후
     2. order-service에게 ItemStockAddRequest 를 보내서 재고를 증가시키게끔 통신한다.
     */
    @Override
    @Transactional
    public ResponseEntity<ResponseDto> cancelOrder(Long orderId) {
        Order order = findByOrderId(orderId);
        Delivery delivery = deliveryService.findByDeliveryId(order.getDeliveryId());
        order.cancel(delivery); // 1.
        List<OrderItem> orderItems = orderItemService.findAllByOrderId(orderId);

        List<ItemStockDto> itemStockDtos = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            itemStockDtos.add(new ItemStockDto(orderItem.getItemId(), orderItem.getCount()));
        }

        ItemStockDtoList itemStockDtoList = new ItemStockDtoList(itemStockDtos);

        itemServiceClient.increaseStock(itemStockDtoList); // 2.

        return ResponseDto.success();
    }

    /**
     1. order 엔티티의 refund 메서드로 delivery 정보를 주어 지금 배송 상태와 날짜 계산 결과로
     판단했을 때 가능하면 상태를 환불 진행 중으로 바꾼다.
     2. 스케줄러가 10초마다 오더의 상태가 환불 진행 중인 것들을 쿼리해와서 날짜 계산 결과가
     환불 신청한 후 1일이 지났으면 재고 증가 처리를 item-service에 통신함으로써 진행한다.
     */
    @Override
    @Transactional
    public ResponseEntity<ResponseDto> refundOrder(Long orderId) {
        Order order = orderRepository.findByOrderId(orderId);
        Delivery delivery = deliveryService.findByDeliveryId(order.getDeliveryId());
        order.refund(delivery);

        return ResponseDto.success();
    }

    /**
     purchaseWishList 흐름
     1. item-service가 넘겨준 주문 정보로 주문을 생성한다.
     2. 넘겨받은 위시리스트에 담긴 상품 정보로 OrderItem을 만들고, 각각 1.에서 생성한 주문과 매핑하여 저장한다.
     3. 주문을 저장한다.
     */
    @Override
    @Transactional
    public ResponseEntity<ResponseDto> purchaseWishList(Long userId, Long addressId, WishListDto wishListDto) {

        Long deliveryId = deliveryService.saveByAddressId(addressId);
        Order order = Order.createOrder(userId, deliveryId);
        Long orderId = save(order);
        List<WishItemDto> wishItemDtos = wishListDto.getWishItemDtos();
        int totalPrice = 0;

        for (WishItemDto wishItemDto : wishItemDtos) {
            OrderItem orderItem = OrderItem.fromWishItemDto(wishItemDto);
            orderItem.mappingOrder(orderId);
            totalPrice += orderItem.getTotalPrice();
            orderItemService.save(orderItem);
        }

        return ResponseDto.success();
    }

    @Override
    public List<Order> findAfterRefund() {
        return orderRepository.findAfterRefund();
    }

    /**
     Order에 포함되는 OrderItem의 totalPrice를 누적합 계산을 함으로써
     Order의 totalPrice를 구하는 메서드

     주문을 마친 후에는 주문 수량을 변경하는 논리는 존재할 수 없으므로 최초에만 계산한다.
     */
    @Override
    @Transactional
    public int getTotalPrice(Long orderId) {
        Order order = findByOrderId(orderId);
        int totalPrice = 0;

        if (order.getTotalPrice() == 0) { // 아직 계산되지 않은 경우에만
            List<OrderItem> orderItems = orderItemService.findAllByOrderId(orderId);
            for (OrderItem orderItem : orderItems) {
                totalPrice += orderItem.getTotalPrice();
            } // 계산해서
            order.mappingTotalPrice(totalPrice); // 결과를 저장해둔다. 즉, 각 Order당 계산 작업은 한 번만 수행한다.
        }

        return order.getTotalPrice();
    }
}
