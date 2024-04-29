package com.itemservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder @Getter
@AllArgsConstructor
public class ItemListDto {

    private String itemName;

    private int price;
}
