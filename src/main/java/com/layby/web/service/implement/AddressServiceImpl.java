package com.layby.web.service.implement;

import com.layby.domain.dto.request.AddressRequestDto;
import com.layby.domain.dto.response.ResponseDto;
import com.layby.domain.entity.AddressEntity;
import com.layby.domain.entity.UserEntity;
import com.layby.domain.repository.AddressRepository;
import com.layby.web.service.AddressService;
import com.layby.web.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserService userService;

    @Override
    public AddressEntity findByAddressId(Long addressId) {
        return addressRepository.findById(addressId).orElse(null);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseDto> updateAddress(Long addressId, AddressRequestDto dto) {
        AddressEntity foundAddressEntity = addressRepository.findById(addressId).orElse(null);
        foundAddressEntity.updateAddressEntity(dto);

        return ResponseDto.successful();
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseDto> addAddress(String username, AddressRequestDto dto) {
        UserEntity userEntity = userService.findByUsername(username);
        AddressEntity addressEntity = AddressEntity.builder()
                .city(dto.getCity())
                .street(dto.getStreet())
                .zipCode(dto.getZipCode())
                .userEntity(userEntity)
                .build();
        addressRepository.save(addressEntity);

        return ResponseDto.successful();
    }
}
