package com.itemservice.web.client;

import com.itemservice.domain.dto.ResponseDto;
import com.itemservice.domain.dto.WishListDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@FeignClient(name = "order-service")
public interface OrderServiceClient {

    @PostMapping("/order/{user_id}")
    ResponseEntity<ResponseDto> makeOrder(
            @PathVariable(name = "user_id") Long userId,
            @RequestBody WishListDto wishListDto
    );


//    @Retry(name = "simpleRetryConfig", fallbackMethod = "retryFallback")
    @CircuitBreaker(name = "simpleCircuitBreakerConfig", fallbackMethod = "circuitFallback")
    @GetMapping("/errorful/case1")
    ResponseEntity<String> case1();

    @GetMapping("/errorful/case2")
    ResponseEntity<String> case2();

    @GetMapping("/errorful/case3")
    ResponseEntity<String> case3();

//    default void retryFallback(Exception ex) {
//        throw new RetryException("재시도에 실패했습니다.");
//    };

    default ResponseEntity<String> circuitFallback(Exception ex) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("현재 주문 서비스에 문제가 있습니다. 잠시 후에 다시 시도해주세요.");
    }
}
