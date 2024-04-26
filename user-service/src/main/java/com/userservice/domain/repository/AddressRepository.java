package com.userservice.domain.repository;

import com.userservice.domain.entity.Address;
import com.userservice.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    Address findByAddressId(Long addressId);

    List<Address> findAllByUserId(Long userId);
}
