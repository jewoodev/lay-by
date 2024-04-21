package com.layby.domain.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AddressUpdateResponseDto extends ResponseDto {
    private AddressUpdateResponseDto() {
        super();
    }

    public static ResponseEntity<AddressUpdateResponseDto> success() {
        AddressUpdateResponseDto responseBody = new AddressUpdateResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
