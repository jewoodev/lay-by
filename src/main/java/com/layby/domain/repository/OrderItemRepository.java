package com.layby.domain.repository;

import com.layby.domain.entity.OrderItem;
import com.layby.domain.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
