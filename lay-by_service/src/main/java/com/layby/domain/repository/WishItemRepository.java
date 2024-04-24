package com.layby.domain.repository;

import com.layby.domain.entity.User;
import com.layby.domain.entity.WishItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishItemRepository extends JpaRepository<WishItem, Long>, WishItemRepositoryCustom {

    WishItem findByWishItemId(Long wishItemId);

    List<WishItem> findAllByUser(User user);
}
