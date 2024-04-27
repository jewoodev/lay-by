package com.itemservice.domain.repository;

import com.itemservice.domain.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Item findByItemId(Long itemId);

    Item findByItemName(String itemName);
}
