package com.layby.domain.entity;


import com.layby.domain.dto.response.WishItemResponseDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "wish_item")
@Entity(name = "wish_item")
public class WishItem {

    @Id @Column(name = "wish_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishItemId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "price")
    private int price;

    @Column(name = "count")
    private int count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    //== 조회 로직 ==//
    /** 주문 상품 전체 가격 조회 **/
    public int getTotalPrice() {
        return this.price * this.count;
    }

    //== 생성 메서드 ==//
    /** 위시리스트에서 사용자가 선택한 위시아이템 Dto를 위시아이템으로 바꾸는 메서드 **/
    public static WishItem covertFromChooseDto(WishItemResponseDto dto, Item item) {
        WishItem wishItem = WishItem.builder()
                .price(dto.getPrice())
                .count(dto.getCount())
                .item(item)
                .build();

        return wishItem;
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
