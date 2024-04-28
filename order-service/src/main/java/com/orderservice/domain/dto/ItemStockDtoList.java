package com.orderservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemStockDtoList {

    List<ItemStockDto> itemStockDtos;
}
