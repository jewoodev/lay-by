package com.layby.web.service;


import com.layby.domain.dto.request.PhoneNumberUpdateRequestDto;
import com.layby.domain.dto.request.UserPasswordUpdateRequestDto;
import com.layby.domain.dto.response.PhoneNumberUpdateResponseDto;
import com.layby.domain.dto.response.UserPasswordUpdateResponseDto;
import com.layby.domain.dto.response.UserResponseDto;
import com.layby.domain.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    User findByUserId(Long userId);

    UserResponseDto referUser(Long userId);

    User findByUsername(String username);

    @Transactional
    ResponseEntity<? super PhoneNumberUpdateResponseDto> updatePhoneNumber(Long userId, PhoneNumberUpdateRequestDto dto);

    @Transactional
    ResponseEntity<? super UserPasswordUpdateResponseDto> updatePassword(Long userId, UserPasswordUpdateRequestDto dto);
}