package com.layby.web.service.implement;

import com.layby.domain.dto.request.AddressRequestDto;
import com.layby.domain.dto.response.AddressListReferResponseDto;
import com.layby.domain.dto.response.ResponseDto;
import com.layby.domain.entity.Address;
import com.layby.domain.entity.User;
import com.layby.domain.repository.AddressRepository;
import com.layby.web.exception.InternalServerErrorException;
import com.layby.web.service.AddressService;
import com.layby.web.service.UserService;
import com.layby.web.util.AES256;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.layby.domain.common.ErrorCode.*;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserService userService;
    private final AES256 personalDataEncoder;

    @Override
    public Address findByAddressId(Long addressId) {
        return addressRepository.findByAddressId(addressId);
    }

    @Override
    public List<Address> findAllByUser(User user) {
        return addressRepository.findAllByUser(user);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseDto> updateAddress(Long addressId, AddressRequestDto dto) {
        Address foundAddress = addressRepository.findById(addressId).orElse(null);
        foundAddress.updateAddress(dto);

        return ResponseDto.success();
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseDto> addAddress(String username, AddressRequestDto dto) {

        String city = dto.getCity();
        String street = dto.getStreet();
        String zipCode = dto.getZipCode();
        String encodedCity = null;
        String encodedStreet = null;
        String encodedZipCode = null;
        String encodedUsername = null;

        try {
            encodedCity = personalDataEncoder.encode(city);
            encodedStreet = personalDataEncoder.encode(street);
            encodedZipCode = personalDataEncoder.encode(zipCode);
            encodedUsername = personalDataEncoder.encode(username);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorException(INTERNAL_SERVER_ERROR.getMessage());
        }

        User user = userService.findByUsername(encodedUsername);
        Address address = Address.builder()
                    .city(encodedCity)
                    .street(encodedStreet)
                    .zipCode(encodedZipCode)
                    .user(user)
                    .build();

        addressRepository.save(address);

        return ResponseDto.success();
    }

    @Override
    public ResponseEntity<AddressListReferResponseDto> referAddressListByUserId(Long userId) {

        User user = userService.findByUserId(userId);
        List<Address> addressList = findAllByUser(user);
        AddressListReferResponseDto addressListReferResponseDto = new AddressListReferResponseDto(addressList);

        return ResponseEntity.status(HttpStatus.OK).body(addressListReferResponseDto);
    }
}
