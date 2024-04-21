package com.layby.domain.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class PhoneNumberUpdateResponseDto extends ResponseDto {
    private PhoneNumberUpdateResponseDto() {
        super();
    }

    public static ResponseEntity<PhoneNumberUpdateResponseDto> success() {
        PhoneNumberUpdateResponseDto responseBody = new PhoneNumberUpdateResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
