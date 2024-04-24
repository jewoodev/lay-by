package com.authservice.web.client;

import com.authservice.domain.dto.response.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/user-service/{user_id}/after-email-cf")
    ResponseEntity<ResponseDto> afterEmailCf(@PathVariable(name = "user_id") Long userId);
}
