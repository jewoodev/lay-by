package com.orderservice.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;

@RestController
@RequestMapping("/errorful")
public class ErrorfulController {

    @GetMapping("/case1")
    public ResponseEntity<String> case1() {
        // Simulate 5% chance of 500 error

        return ResponseEntity.status(500).body("Internal Server Error");

//        String now = String.valueOf(LocalDateTime.now());
//
//        try {
//            Thread.sleep(80000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(now);
    }

    @GetMapping("/case2")
    public ResponseEntity<String> case2() {
        // Simulate blocking requests every first 10 seconds
        LocalTime currentTime = LocalTime.now();
        int currentSecond = currentTime.getSecond();

        if (currentSecond < 10) {
            // Simulate a delay (block) for 10 seconds
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return ResponseEntity.status(503).body("Service Unavailable");
        }

        return ResponseEntity.ok("Normal response");
    }

    @GetMapping("/case3")
    public ResponseEntity<String> case3() {
        // Simulate 500 error every first 10 seconds
        LocalTime currentTime = LocalTime.now();
        int currentSecond = currentTime.getSecond();

        if (currentSecond < 10) {
            return ResponseEntity.status(500).body("Internal Server Error");
        }

        return ResponseEntity.ok("Normal response");
    }
}
