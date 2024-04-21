package com.layby.web.service;


import com.layby.domain.dto.request.PhoneNumberUpdateRequestDto;
import com.layby.domain.dto.request.UserPasswordUpdateRequestDto;
import com.layby.domain.dto.response.PhoneNumberUpdateResponseDto;
import com.layby.domain.dto.response.ResponseDto;
import com.layby.domain.dto.response.UserPasswordUpdateResponseDto;
import com.layby.domain.dto.response.UserResponseDto;
import com.layby.domain.entity.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    UserEntity findByUserId(Long userId);

    UserResponseDto referUser(Long userId);

    UserEntity findByUsername(String username);

    @Transactional
    ResponseEntity<PhoneNumberUpdateResponseDto> updatePhoneNumber(Long userId, PhoneNumberUpdateRequestDto dto);

    @Transactional
    ResponseEntity<UserPasswordUpdateResponseDto> updatePassword(Long userId, UserPasswordUpdateRequestDto dto);
}