package com.orderservice.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemStockRequest {

    private Long itemId;

    private int count;
}
