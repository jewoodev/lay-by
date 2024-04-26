package com.userservice.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserDto {

    @JsonIgnore
    private Long userId;

    private String username;

    private String email;

    private String phoneNumber;

    @Builder
    public UserDto(Long userId, String username, String email, String phoneNumber) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
