package com.layby.domain.repository;

import com.layby.domain.entity.User;
import com.layby.domain.entity.WishItem;

import java.util.List;

public interface WishItemRepositoryCustom {

    List<WishItem> findAllByUserIdWithItem(Long userId);
}
