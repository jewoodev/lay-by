package com.orderservice.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishItemDto {

    private Long wishItemId;

    private String itemName;

    private int price;

    private int count;

    private int totalPrice;

    private Long itemId;

    private Long userId;
}
