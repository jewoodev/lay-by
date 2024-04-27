package com.itemservice.domain.repository;

import com.itemservice.domain.entity.WishItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishItemRepository extends JpaRepository<WishItem, Long> {

    WishItem findByWishItemId(Long wishItemId);

    List<WishItem> findAllByUserId(Long userId);
}
