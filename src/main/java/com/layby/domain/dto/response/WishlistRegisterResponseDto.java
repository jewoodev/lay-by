package com.layby.domain.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class WishlistRegisterResponseDto extends ResponseDto {

    private WishlistRegisterResponseDto() {
        super();
    }

    public static ResponseEntity<WishlistRegisterResponseDto> success() {
        WishlistRegisterResponseDto responseBody = new WishlistRegisterResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
