package com.layby.domain.dto.response;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ItemSaveResponseDto extends ResponseDto {

    private ItemSaveResponseDto() {
        super();
    }

    public static ResponseEntity<ItemSaveResponseDto> success() {
        ItemSaveResponseDto responseBody = new ItemSaveResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
