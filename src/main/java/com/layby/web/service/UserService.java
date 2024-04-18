package com.layby.web.service;

import com.layby.domain.dto.UserDto;
import com.layby.domain.entity.Authority;
import com.layby.domain.entity.User;
import com.layby.web.exception.DuplicateMemberException;
import com.layby.web.exception.NotFoundMemberException;
import com.layby.domain.repository.UserRepository;
import com.layby.web.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

public interface UserService {

    UserDto signup(UserDto userDto);

    UserDto getUserWithAuthorities(String username);

    UserDto getMyUserWithAuthorities();
}