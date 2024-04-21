package com.layby.domain.dto.response;

import com.layby.domain.entity.WishItem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class WishItemResponseDto {

    private String itemName;

    private int price;

    private int count;

    private int totalPrice;

    public static WishItemResponseDto fromOrderItemEntity(WishItem wishItem) {

        WishItemResponseDto wishItemResponseDto = WishItemResponseDto.builder()
                .itemName(wishItem.getItem().getItemName())
                .price(wishItem.getPrice())
                .count(wishItem.getCount())
                .totalPrice(wishItem.getTotalPrice())
                .build();

        return wishItemResponseDto;
    }
}
