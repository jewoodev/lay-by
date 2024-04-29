package com.itemservice.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.itemservice.domain.dto.WishItemDto;

@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "wish_item")
@Entity(name = "wish_item")
public class WishItem {

    @Id @Column(name = "wish_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishItemId;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "price")
    private int price;

    @Column(name = "count")
    private int count;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "item_id")
    private Long itemId;

    //== 조회 로직 ==//
    /** 주문 상품 전체 가격 조회 **/
    public int getTotalPrice() {
        return this.price * this.count;
    }

    //== 생성 메서드 ==//
    /** 위시리스트에서 사용자가 선택한 위시아이템 Dto를 위시아이템으로 바꾸는 메서드 **/
    public static WishItem covertFromChooseDto(WishItemDto dto) {
        return WishItem.builder()
                .itemName(dto.getItemName())
                .price(dto.getPrice())
                .count(dto.getCount())
                .userId(dto.getUserId())
                .itemId(dto.getItemId())
                .build();
    }

    //== 비즈니스 로직 ==//

    /** 수량 up **/
    public void increaseCount() {
        this.count++;
    }

    public void decreaseCount() {
        this.count--;
    }
}
