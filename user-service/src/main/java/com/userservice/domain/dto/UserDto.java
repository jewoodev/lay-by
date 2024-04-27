package com.userservice.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @JsonIgnore
    private Long userId;

    private String username;

    @JsonIgnore
    private String password;

    private String email;

    private String phoneNumber;
}
