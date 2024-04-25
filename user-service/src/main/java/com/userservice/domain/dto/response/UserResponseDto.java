package com.userservice.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserResponseDto {

    @JsonIgnore
    private Long userId;

    private String username;

    private String email;

    private String phoneNumber;

    @Builder
    public UserResponseDto(Long userId, String username, String email, String phoneNumber) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
