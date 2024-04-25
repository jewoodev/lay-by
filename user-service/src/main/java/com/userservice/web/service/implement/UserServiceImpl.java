package com.userservice.web.service.implement;

import com.userservice.domain.common.ErrorCode;
import com.userservice.domain.dto.ResponseDto;
import com.userservice.domain.dto.request.PhoneNumberUpdateRequestDto;
import com.userservice.domain.dto.request.UserPasswordUpdateRequestDto;
import com.userservice.domain.dto.response.UserResponseDto;
import com.userservice.domain.entity.User;
import com.userservice.domain.repository.UserRepository;
import com.userservice.web.exception.AES256Exception;
import com.userservice.web.service.UserService;
import com.userservice.web.util.AES256;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.userservice.domain.common.ErrorCode.*;
import static com.userservice.domain.common.ErrorCode.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AES256 personalDataEncoder;


    @Override
    public User findByUserId(Long userId) {
        return userRepository.findByUserId(userId);
    }

    @Override
    public ResponseEntity<UserResponseDto> referUser(Authentication authentication) {
        String username = authentication.getPrincipal().toString();
        User user = userRepository.findByUsername(username);

        String encodedEmail = user.getEmail();
        String encodedPhoneNumber = user.getPhoneNumber();
        String email = null;
        String phoneNumber = null;

        log.info("encodedPhoneNumber = {}", encodedPhoneNumber);

        try {
            email = personalDataEncoder.decode(encodedEmail);
            phoneNumber = personalDataEncoder.decode(encodedPhoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AES256Exception(INTERNAL_SERVER_ERROR.getMessage());
        }

        UserResponseDto responseBody = UserResponseDto.builder()
                                                .userId(user.getUserId())
                                                .username(username)
                                                .email(email)
                                                .phoneNumber(phoneNumber)
                                                .build();

        return ResponseEntity.status(OK).body(responseBody);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    @Override
    public ResponseEntity<ResponseDto> updatePhoneNumber(Long userId, PhoneNumberUpdateRequestDto dto) {
        User foundUser = userRepository.findByUserId(userId);

        String phoneNumber = dto.getPhoneNumber();
        String encodedPhoneNumber = null;

        try {
            encodedPhoneNumber  = personalDataEncoder.encode(phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AES256Exception(INTERNAL_SERVER_ERROR.getMessage());
        }

        foundUser.updatePhoneNumber(encodedPhoneNumber);

        return ResponseDto.success();
    }

    @Override
    public ResponseEntity<ResponseDto> updatePassword(Long userId, UserPasswordUpdateRequestDto dto) {
        User foundUser = userRepository.findByUserId(userId);

        String password = dto.getPassword();
        String encodedPassword = null;

        try {
            encodedPassword = personalDataEncoder.encode(password);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AES256Exception(INTERNAL_SERVER_ERROR.getMessage());
        }

        foundUser.updatePassword(encodedPassword);

        return ResponseDto.success();
    }

    @Override
    public ResponseEntity<ResponseDto> updateRoleAfterEmailCF(Long userId) {
        User user = userRepository.findByUserId(userId);

        user.updateRoleAfterEmailCF();

        return ResponseDto.success();
    }
}