package com.orderservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemStockDto {

    private Long itemId;

    private int count;
}