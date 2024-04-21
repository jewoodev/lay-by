package com.layby.domain.entity;


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

    @Column(name = "price")
    private int price;

    @Column(name = "count")
    private int count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wish_list_id")
    private WishList wishList;

    //== 조회 로직 ==//
    /** 주문 상품 전체 가격 조회 **/
    public int getTotalPrice() {
        return this.price * this.count;
    }
}
