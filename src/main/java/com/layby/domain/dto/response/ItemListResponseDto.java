package com.layby.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder @Getter
@AllArgsConstructor
public class ItemListResponseDto {

    private String itemName;

    private int price;
}
