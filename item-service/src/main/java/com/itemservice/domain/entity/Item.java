package com.itemservice.domain.entity;

import com.itemservice.domain.common.ErrorCode;
import com.itemservice.web.exception.NotEnoughStockException;
import com.itemservice.domain.vo.ItemRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.itemservice.domain.common.ErrorCode.*;


@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "item")
@Entity(name = "item")
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "price")
    private int price;

    @Column(name = "details")
    private String details;

    @Column(name = "stock_quantity")
    private int stockQuantity;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0)
            throw new NotEnoughStockException(NOT_ENOUGH_STOCK.getMessage());
        this.stockQuantity = restStock;
    }

    public static Item fromRequest(ItemRequest vo) {
        return Item.builder()
                .itemName(vo.getItemName())
                .price(Integer.parseInt(vo.getPrice()))
                .details(vo.getDetails())
                .stockQuantity(Integer.parseInt(vo.getStockQuantity()))
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();
    }
}
