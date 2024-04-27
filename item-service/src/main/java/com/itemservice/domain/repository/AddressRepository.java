package com.itemservice.domain.repository;

import com.itemservice.domain.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    Address findByAddressId(Long addressId);

    List<Address> findAllByUserId(Long userId);
}
