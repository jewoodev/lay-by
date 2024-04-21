package com.layby.web.service.implement;

import com.layby.domain.common.ErrorCode;
import com.layby.domain.dto.request.AddressRequestDto;
import com.layby.domain.dto.response.AddressUpdateResponseDto;
import com.layby.domain.dto.response.ResponseDto;
import com.layby.domain.entity.AddressEntity;
import com.layby.domain.entity.UserEntity;
import com.layby.domain.repository.AddressRepository;
import com.layby.web.exception.InternalServerErrorException;
import com.layby.web.service.AddressService;
import com.layby.web.service.UserService;
import com.layby.web.util.AES256;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.layby.domain.common.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserService userService;
    private final AES256 personalDataEncoder;

    @Override
    public AddressEntity findByAddressId(Long addressId) {
        return addressRepository.findById(addressId).orElse(null);
    }

    @Override
    @Transactional
    public ResponseEntity<AddressUpdateResponseDto> updateAddress(Long addressId, AddressRequestDto dto) {
        AddressEntity foundAddressEntity = addressRepository.findById(addressId).orElse(null);
        foundAddressEntity.updateAddressEntity(dto);

        return AddressUpdateResponseDto.success();
    }

    @Override
    @Transactional
    public ResponseEntity<AddressUpdateResponseDto> addAddress(String username, AddressRequestDto dto) {

        String encodedUsername = null;

        try {
            encodedUsername = personalDataEncoder.encode(username);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorException(INTERNAL_SERVER_ERROR.getMessage());
        }

        log.info("dto's city = {}", dto.getCity());
        log.info("dto's street = {}", dto.getStreet());
        log.info("dto's zipCode = {}", dto.getZipCode());

        UserEntity userEntity = userService.findByUsername(encodedUsername);
        AddressEntity addressEntity = AddressEntity.builder()
                .city(dto.getCity())
                .street(dto.getStreet())
                .zipCode(dto.getZipCode())
                .userEntity(userEntity)
                .build();
        addressRepository.save(addressEntity);

        return AddressUpdateResponseDto.success();
    }
}
