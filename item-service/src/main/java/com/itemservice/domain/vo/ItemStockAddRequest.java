package com.itemservice.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemStockAddRequest {

    private Long itemId;

    private int count;
}
