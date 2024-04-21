package com.layby.domain.repository;

import com.layby.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<User, Long> {
}
