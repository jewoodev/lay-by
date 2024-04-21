package com.layby.domain.repository;

import com.layby.domain.entity.User;
import com.layby.domain.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<WishList, Long> {

    WishList findByUser(User user);
}
