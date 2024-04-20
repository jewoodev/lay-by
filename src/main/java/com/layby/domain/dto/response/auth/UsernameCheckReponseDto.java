package com.layby.domain.dto.response.auth;

import com.layby.domain.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class UsernameCheckReponseDto extends ResponseDto {

    private UsernameCheckReponseDto() {
        super();
    }

    public static ResponseEntity<UsernameCheckReponseDto> success() {
        UsernameCheckReponseDto responseDto = new UsernameCheckReponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
