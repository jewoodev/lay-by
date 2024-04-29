package com.itemservice.domain.dto;

import com.itemservice.domain.entity.Address;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class AddressListDto {

    private List<AddressDto> addressDtos = new ArrayList<>();

    //== 생성자 ==//
    /** Address 엔티티 리스트로 AddressListReferResponseDto를 생성하는 생성자 **/
    public AddressListDto(List<Address> addressList) {

        for (Address address : addressList) {
            AddressDto addressDto = new AddressDto(address);
            this.addressDtos.add(addressDto);
        }
    }
}
