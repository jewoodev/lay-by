package com.layby.domain.repository;

import com.layby.domain.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    User findByUserId(Long userId);

    User findByUsername(String username);
}
