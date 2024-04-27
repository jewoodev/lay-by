package com.orderservice.web.client;

import com.orderservice.domain.dto.ResponseDto;
import com.orderservice.domain.vo.ItemStockAddRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "item-service")
public interface ItemServiceClient {

    @PatchMapping("/item-service/item/{item_id}")
    ResponseEntity<ResponseDto> increaseStock(List<ItemStockAddRequest> requests);
}
