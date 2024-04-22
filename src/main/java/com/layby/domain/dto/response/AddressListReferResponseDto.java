package com.layby.domain.dto.response;

import com.layby.domain.entity.Address;
import com.layby.domain.entity.WishItem;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class AddressListReferResponseDto {

    private List<AddressReferResponseDto> addressReferResponseDtos = new ArrayList<>();

    public AddressListReferResponseDto(List<Address> addressList) {

        for (Address address : addressList) {
            AddressReferResponseDto addressReferResponseDto = new AddressReferResponseDto(address);
            this.addressReferResponseDtos.add(addressReferResponseDto);
        }
    }
}
