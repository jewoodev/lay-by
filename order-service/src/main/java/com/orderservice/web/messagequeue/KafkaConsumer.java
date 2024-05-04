package com.orderservice.web.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderservice.domain.dto.WishItemDto;
import com.orderservice.domain.dto.WishListDto;
import com.orderservice.domain.entity.Order;
import com.orderservice.domain.entity.OrderItem;
import com.orderservice.web.service.OrderItemService;
import com.orderservice.web.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaConsumer {

    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @KafkaListener(topics = "make-order")
    public void makeOrder(String kafkaMessage) {
        log.info("Kafka message : {}", kafkaMessage);

        WishListDto wishListDto = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            wishListDto = mapper.readValue(kafkaMessage, WishListDto.class);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        log.info("Convert to : {}", wishListDto);

        Long userId = wishListDto.getWishItemDtos().get(0).getUserId();
        Order order = Order.createOrder(userId);
        Long orderId = orderService.save(order);

        List<WishItemDto> wishItemDtos = wishListDto.getWishItemDtos();

        for (WishItemDto wishItemDto : wishItemDtos) {
            OrderItem orderItem = OrderItem.fromWishItemDto(wishItemDto);
            orderItem.mappingOrder(orderId);
            orderItemService.save(orderItem);
        }
    }
}
