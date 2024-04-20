package com.layby.web.service;

import com.layby.domain.dto.UserDto;
import com.layby.domain.dto.request.auth.SignInRequestDto;
import com.layby.domain.dto.request.auth.SignUpRequestDto;
import com.layby.domain.dto.response.auth.SignInResponseDto;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    UserDto signup(SignUpRequestDto dto);

    void signIn(SignInRequestDto dto);

    UserDto getUserWithAuthorities(String username);

    UserDto getMyUserWithAuthorities();
}