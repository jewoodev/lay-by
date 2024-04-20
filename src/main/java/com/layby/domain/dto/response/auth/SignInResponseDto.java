package com.layby.domain.dto.response.auth;

import com.layby.domain.common.ResponseCode;
import com.layby.domain.common.ResponseMessage;
import com.layby.domain.dto.response.ResponseDto;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class SignInResponseDto extends ResponseDto {

    private String token;

    private SignInResponseDto(String token) {
        super();
        this.token = token;
    }

    public static ResponseEntity<SignInResponseDto> success(String token) {
        SignInResponseDto responseBody = new SignInResponseDto(token);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> signInFail() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.SIGN_IN_FAIL, ResponseMessage.SIGN_IN_FAIL);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }
}
