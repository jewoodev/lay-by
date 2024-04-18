package com.layby.domain.dto.response.auth;

import com.layby.domain.common.ResponseCode;
import com.layby.domain.common.ResponseMessage;
import com.layby.domain.dto.response.ResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.yaml.snakeyaml.emitter.Emitable;

@Getter
public class EmailCertificationResponseDto extends ResponseDto {

    private EmailCertificationResponseDto() {
        super();
    }

    public static ResponseEntity<EmailCertificationResponseDto> success() {
        EmailCertificationResponseDto responseBody = new EmailCertificationResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> duplicateId() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.DUPLICATED_ID, ResponseMessage.DUPLICATE_ID);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> mailSendFail() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.MAIL_FAIL, ResponseMessage.MAIL_FAIL);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
}
