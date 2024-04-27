package com.orderservice.web.service.implement;

import com.orderservice.domain.dto.OrderStatusDto;
import com.orderservice.domain.dto.ResponseDto;
import com.orderservice.domain.dto.WishItemDto;
import com.orderservice.domain.dto.WishListDto;
import com.orderservice.domain.entity.Delivery;
import com.orderservice.domain.entity.Order;
import com.orderservice.domain.entity.OrderItem;
import com.orderservice.domain.vo.ItemStockAddRequest;
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
            Delivery delivery = deliveryService.findByOrderId(order.getOrderId());
            OrderStatusDto orderStatusDto = OrderStatusDto.convertToStatusDto(order, delivery.getDeliveryStatus());
            responseBody.add(orderStatusDto);
        }

        // 반환한다.
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    /**
     주문 취소 메서드.
     1. 주문 취소가 가능한지 order에 지정된 delivery status를 확인해 판단하고 가능하면 상태를 취소로 바꾼 후
     2. order-service에게 ItemStockAddRequest 를 보내서 재고를 증가시키게끔 통신한다.
     */
    @Override
    @Transactional
    public ResponseEntity<ResponseDto> cancelOrder(Long orderId) {
        Delivery delivery = deliveryService.findByOrderId(orderId);
        Order order = findByOrderId(orderId);
        order.cancel(delivery); // 1.

        List<OrderItem> orderItems = orderItemService.findAllByOrderId(orderId);

        List<ItemStockAddRequest> requests = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            requests.add(new ItemStockAddRequest(orderItem.getItemId(), orderItem.getCount()));
        }

        itemServiceClient.increaseStock(requests); // 2.

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
    public ResponseEntity<ResponseDto> purchaseWishList(Long userId, Long addressId, WishListDto wishListDto) {

        Long deliveryId = deliveryService.saveByAddressId(addressId);
        Order order = Order.createOrder(userId, deliveryId);
        List<WishItemDto> wishItemDtos = wishListDto.getWishItemDtos();
        int totalPrice = 0;

        for (WishItemDto wishItemDto : wishItemDtos) {
            OrderItem orderItem = OrderItem.fromWishItemDto(wishItemDto);
            orderItem.mappingOrder(order.getOrderId());
            totalPrice += orderItem.getTotalPrice();
            orderItemService.save(orderItem);
        }

        order.mappingTotalPrice(totalPrice);
        save(order);

        return ResponseDto.success();
    }

    @Override
    public List<Order> findAfterRefund() {
        return orderRepository.findAfterRefund();
    }


}
