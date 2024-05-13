package com.itemservice.domain.dto;

import com.itemservice.domain.entity.WishItem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter @Setter
@ToString
public class WishListDto {

    private List<WishItemDto> wishItemDtos;

    private int totalPrice = 0;

    //== 생성자 메서드 ==//
    public static WishListDto fromWishItemDtos(List<WishItemDto> dtos) {
        int totalPrice = 0;

        for (WishItemDto dto : dtos) {
            totalPrice += dto.getTotalPrice();
        }

        return WishListDto.builder()
                .wishItemDtos(dtos)
                .totalPrice(totalPrice)
                .build();
    }

    public static WishListDto fromWishItems(List<WishItem> wishItems) {
        int totalPrice = 0;
        List<WishItemDto> wishItemDtos = new ArrayList<>();

        for (WishItem wishItem : wishItems) {
            wishItemDtos.add(WishItemDto.fromWishItem(wishItem));
            totalPrice += wishItem.getTotalPrice();
        }

        return WishListDto.builder()
                .wishItemDtos(wishItemDtos)
                .totalPrice(totalPrice)
                .build();
    }
}
