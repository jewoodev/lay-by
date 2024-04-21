package com.layby.domain.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UserPasswordUpdateResponseDto extends ResponseDto {
    private UserPasswordUpdateResponseDto() {
        super();
    }

    public static ResponseEntity<UserPasswordUpdateResponseDto> success() {
        UserPasswordUpdateResponseDto responseBody = new UserPasswordUpdateResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
