package com.orderservice.domain.repository;

import com.orderservice.domain.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    Address findByAddressId(Long addressId);

    List<Address> findAllByUserId(Long userId);
}
