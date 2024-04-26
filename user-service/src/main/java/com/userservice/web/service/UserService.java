package com.userservice.web.service;

import com.userservice.domain.dto.ResponseDto;
import com.userservice.domain.vo.PhoneNumberUpdateRequest;
import com.userservice.domain.vo.UserPasswordUpdateRequest;
import com.userservice.domain.dto.UserDto;
import com.userservice.domain.entity.User;
import org.springframework.http.ResponseEntity;

public interface UserService {

    User findByUserId(Long userId);

    ResponseEntity<UserDto> referUser(Long userId);

    User findByUsername(String username);

    ResponseEntity<ResponseDto> updatePhoneNumber(Long userId, PhoneNumberUpdateRequest dto);

    ResponseEntity<ResponseDto> updatePassword(Long userId, UserPasswordUpdateRequest dto);

    ResponseEntity<ResponseDto> updateRoleAfterEmailCF(Long userId);

    boolean existsByUsername(String username);

    Long save(User user);
}