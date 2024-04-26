package com.userservice.web.service.implement;

import com.userservice.domain.dto.AddressListDto;
import com.userservice.domain.dto.ResponseDto;
import com.userservice.domain.entity.Address;
import com.userservice.domain.entity.User;
import com.userservice.domain.repository.AddressRepository;
import com.userservice.domain.vo.AddressRequest;
import com.userservice.web.exception.InternalServerErrorException;
import com.userservice.web.service.UserService;
import com.userservice.web.util.PersonalDataEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.userservice.web.service.AddressService;

import java.util.List;

import static com.userservice.domain.common.ErrorCode.INTERNAL_SERVER_ERROR;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserService userService;
    private final PersonalDataEncoder personalDataEncoder;

    @Override
    public Address findByAddressId(Long addressId) {
        return addressRepository.findByAddressId(addressId);
    }

    @Override
    public List<Address> findAllByUserId(Long userId) {
        return addressRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseDto> updateAddress(Long addressId, AddressRequest vo) {
        Address foundAddress = addressRepository.findById(addressId).orElse(null);
        foundAddress.updateAddress(vo);

        return ResponseDto.success();
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseDto> addAddress(Long userId, AddressRequest vo) {

        String city = vo.getCity();
        String street = vo.getStreet();
        String zipCode = vo.getZipCode();
        String encodedCity = null;
        String encodedStreet = null;
        String encodedZipCode = null;

        try {
            encodedCity = personalDataEncoder.encode(city);
            encodedStreet = personalDataEncoder.encode(street);
            encodedZipCode = personalDataEncoder.encode(zipCode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorException(INTERNAL_SERVER_ERROR.getMessage());
        }

        Address address = Address.builder()
                    .city(encodedCity)
                    .street(encodedStreet)
                    .zipCode(encodedZipCode)
                    .userId(userId)
                    .build();

        addressRepository.save(address);

        return ResponseDto.success();
    }

    @Override
    public ResponseEntity<AddressListDto> referAddressListByUserId(Long userId) {

        User user = userService.findByUserId(userId);
        List<Address> addressList = findAllByUserId(userId);
        AddressListDto addressListDto = new AddressListDto(addressList);

        return ResponseEntity.status(HttpStatus.OK).body(addressListDto);
    }
}
