package com.userservice.web.service;


import com.userservice.domain.dto.ResponseDto;
import com.userservice.domain.dto.request.PhoneNumberUpdateRequestDto;
import com.userservice.domain.dto.request.UserPasswordUpdateRequestDto;
import com.userservice.domain.dto.response.UserResponseDto;
import com.userservice.domain.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    User findByUserId(Long userId);

    ResponseEntity<UserResponseDto> referUser(Authentication authentication);

    User findByUsername(String username);

    ResponseEntity<ResponseDto> updatePhoneNumber(Long userId, PhoneNumberUpdateRequestDto dto);

    ResponseEntity<ResponseDto> updatePassword(Long userId, UserPasswordUpdateRequestDto dto);

    ResponseEntity<ResponseDto> updateRoleAfterEmailCF(Long userId);
}