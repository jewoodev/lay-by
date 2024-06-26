package com.layby.web.service.implement;

import com.layby.domain.common.ErrorCode;
import com.layby.domain.dto.response.OrderStatusResponseDto;
import com.layby.domain.dto.response.ResponseDto;
import com.layby.domain.entity.Order;
import com.layby.domain.entity.User;
import com.layby.domain.repository.OrderRepository;
import com.layby.web.exception.InternalServerErrorException;
import com.layby.web.service.OrderService;
import com.layby.web.service.UserService;
import com.layby.web.util.AES256;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final AES256 personalDataEncoder;

    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }

    @Override
    public Order findByOrderId(Long orderId) {
        return orderRepository.findByOrderId(orderId);
    }

    @Override
    public List<Order> findAtferRefund() {
        return orderRepository.findAfterRefund();
    }

    @Override
    public ResponseEntity<List<OrderStatusResponseDto>> referOrdersStatus(Authentication authentication) {
        String username = authentication.getPrincipal().toString();
        String encodedUsername = null;

        try {
            encodedUsername = personalDataEncoder.encode(username);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorException(ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        }

        // 현재 인증되어진 유저의 전체 주문 목록을 조회해서
        User user = userService.findByUsername(encodedUsername);
        List<Order> allByUser = orderRepository.findAllByUserIdWithOrderItemAndItem(user.getUserId());
        List<OrderStatusResponseDto> responseBody = new ArrayList<>();

        // Dto로 변환해서
        for (Order order : allByUser) {
            OrderStatusResponseDto orderStatusResponseDto = Order.convertToStatusDto(order);
            responseBody.add(orderStatusResponseDto);
        }

        // 반환한다.
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseDto> cancelOrder(Long orderId) {
        Order order = orderRepository.findByOrderId(orderId);
        order.cancel();

        return ResponseDto.success();
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseDto> refundOrder(Long orderId) {
        Order order = orderRepository.findByOrderIdWithDelivery(orderId);
        order.refund();

        return ResponseDto.success();
    }
}
