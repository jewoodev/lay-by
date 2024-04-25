package com.layby.domain.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserResponseDto {


    private String phoneNumber;

    public UserResponseDto(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
