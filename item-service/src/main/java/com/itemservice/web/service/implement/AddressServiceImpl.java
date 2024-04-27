package com.itemservice.web.service.implement;

import com.itemservice.domain.dto.AddressListDto;
import com.itemservice.domain.dto.ResponseDto;
import com.itemservice.domain.entity.Address;
import com.itemservice.domain.repository.AddressRepository;
import com.itemservice.domain.vo.AddressRequest;
import com.itemservice.web.service.AddressService;
import com.itemservice.web.util.PersonalDataEncoder;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.itemservice.domain.common.ErrorCode.INTERNAL_SERVER_ERROR;


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
