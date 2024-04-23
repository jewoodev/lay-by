package com.layby.domain.repository;

import com.layby.domain.entity.Address;
import com.layby.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    Address findByAddressId(Long addressId);

    List<Address> findAllByUser(User user);
}
