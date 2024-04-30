package com.itemservice.web.controller;

import com.itemservice.web.client.OrderServiceClient;
import feign.Request;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Order;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/error-test")
public class ErrorfulController {

    private final OrderServiceClient orderServiceClient;

    @GetMapping("/test-case1")
    public ResponseEntity<String> case1() {
        return orderServiceClient.case1();
    }

    @GetMapping("/test-case2")
    public ResponseEntity<String> case2() {
        return orderServiceClient.case2();
    }

    @GetMapping("/test-case3")
    public ResponseEntity<String> case3() {
        return orderServiceClient.case3();
    }

}
