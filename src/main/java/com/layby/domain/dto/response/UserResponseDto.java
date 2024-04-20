package com.layby.domain.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserResponseDto {

    private String password;

    private String phoneNumber;

    public UserResponseDto(String password, String phoneNumber) {
        this.password = password;
        this.phoneNumber = phoneNumber;
    }
}
