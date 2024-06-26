package com.orderservice.web.client;

import com.orderservice.domain.dto.ItemStockDto;
import com.orderservice.domain.dto.ItemStockDtoList;
import com.orderservice.domain.dto.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "item-service")
public interface ItemServiceClient {

    @PutMapping("/item/ic")
    ResponseEntity<ResponseDto> increaseStock(@RequestBody ItemStockDtoList itemStockDtos);

    @PutMapping("/item/dc")
    ResponseEntity<ResponseDto> decreaseStock(@RequestBody ItemStockDtoList itemStockDtos);
}
