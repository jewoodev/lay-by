package com.orderservice.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter @Setter
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
}
