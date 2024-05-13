package com.orderservice.domain.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter @Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WishListDto {

    private List<WishItemDto> wishItemDtos;

    private int totalPrice = 0;

    //== 생성 메서드 ==//
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
