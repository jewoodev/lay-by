package com.itemservice.domain.repository;

import com.itemservice.domain.entity.Item;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Item findByItemId(Long itemId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from item i where i.itemId = :itemId")
    Item findByItemIdWithPMCLock(Long itemId);

    Item findByItemName(String itemName);
}
