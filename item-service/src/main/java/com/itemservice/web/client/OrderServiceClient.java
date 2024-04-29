package com.itemservice.web.client;

import com.itemservice.domain.dto.ResponseDto;
import com.itemservice.domain.dto.WishListDto;
import com.itemservice.web.handler.FeignErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "order-service", configuration = FeignErrorDecoder.class)
public interface OrderServiceClient {

    @PostMapping("/order/{user_id}")
    ResponseEntity<ResponseDto> purchaseWishList(
            @PathVariable(name = "user_id") Long userId,
            @RequestParam Long addressId,
            @RequestBody WishListDto wishListDto
    );
}
