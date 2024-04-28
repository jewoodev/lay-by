package com.itemservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemStockDto {

    private Long itemId;

    private int count;
}
