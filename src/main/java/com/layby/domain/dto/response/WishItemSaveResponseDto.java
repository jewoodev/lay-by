package com.layby.domain.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class WishItemSaveResponseDto extends ResponseDto {

    private WishItemSaveResponseDto() {
        super();
    }

    public static ResponseEntity<WishItemSaveResponseDto> success() {
        WishItemSaveResponseDto responseBody = new WishItemSaveResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
