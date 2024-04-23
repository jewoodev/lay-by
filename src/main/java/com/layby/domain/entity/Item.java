package com.layby.domain.entity;

import com.layby.domain.dto.request.ItemSaveRequestDto;
import com.layby.web.exception.NotEnoughStockException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.layby.domain.common.ErrorCode.NOT_ENOUGH_STOCK;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "item")
@Entity(name = "item")
public class Item extends BaseTimeEntity {

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

    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0)
            throw new NotEnoughStockException(NOT_ENOUGH_STOCK.getMessage());
        this.stockQuantity = restStock;
    }

    public Item(ItemSaveRequestDto dto) {
        this.itemName = dto.getItemName();
        this.price = Integer.parseInt(dto.getPrice());
        this.details = dto.getDetails();
        this.stockQuantity = Integer.parseInt(dto.getStockQuantity());
    }
}
