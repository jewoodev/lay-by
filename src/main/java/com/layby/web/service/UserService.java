package com.layby.web.service;

import com.layby.domain.dto.UserDto;

public interface UserService {

    UserDto signup(UserDto userDto);

    UserDto getUserWithAuthorities(String username);

    UserDto getMyUserWithAuthorities();
}