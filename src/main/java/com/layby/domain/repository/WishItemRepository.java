package com.layby.domain.repository;

import com.layby.domain.entity.WishItem;
import com.layby.domain.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishItemRepository extends JpaRepository<WishItem, Long> {

    List<WishItem> findAllByWishList(WishList wishList);
}
