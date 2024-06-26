package com.orderservice.web.service.implement;

import com.orderservice.domain.dto.AddressListDto;
import com.orderservice.domain.dto.ResponseDto;
import com.orderservice.domain.entity.Address;
import com.orderservice.domain.repository.AddressRepository;
import com.orderservice.domain.vo.AddressRequest;
import com.orderservice.web.service.AddressService;
import com.orderservice.web.util.PersonalDataEncoder;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.orderservice.domain.common.ErrorCode.INTERNAL_SERVER_ERROR;


@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
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
                    .createdDate(LocalDateTime.now())
                    .modifiedDate(LocalDateTime.now())
                    .build();

        addressRepository.save(address);

        return ResponseDto.success();
    }

    @Override
    public ResponseEntity<AddressListDto> referAddressListByUserId(Long userId) {

        List<Address> addressList = findAllByUserId(userId);
        AddressListDto addressListDto = new AddressListDto(addressList);

        return ResponseEntity.status(HttpStatus.OK).body(addressListDto);
    }
}
