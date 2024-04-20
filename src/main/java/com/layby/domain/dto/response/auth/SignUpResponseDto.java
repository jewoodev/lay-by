package com.layby.domain.dto.response.auth;

import com.layby.domain.common.ResponseCode;
import com.layby.domain.common.ResponseMessage;
import com.layby.domain.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class SignUpResponseDto extends ResponseDto {
    private SignUpResponseDto() {
        super();
    }

    public static ResponseEntity<SignUpResponseDto> success() {
        SignUpResponseDto responseBody = new SignUpResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> duplicatedUsername() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.DUPLICATED_USERNAME, ResponseMessage.DUPLICATE_USERNAME);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> certificationFail() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.CERTIFICATION_FAIL, ResponseMessage.CERTIFICATION_FAIL);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> internalError() {
        ResponseDto responseBody =
                new ResponseDto(ResponseCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }
}
