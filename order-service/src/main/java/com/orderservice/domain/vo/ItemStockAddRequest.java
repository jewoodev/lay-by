package com.orderservice.domain.vo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ItemStockAddRequest {

    private Long itemId;

    private int count;
}
