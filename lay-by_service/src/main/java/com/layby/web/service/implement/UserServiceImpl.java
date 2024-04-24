package com.layby.web.service.implement;


import com.layby.domain.common.ErrorCode;
import com.layby.domain.dto.request.PhoneNumberUpdateRequestDto;
import com.layby.domain.dto.request.UserPasswordUpdateRequestDto;
import com.layby.domain.dto.response.ResponseDto;
import com.layby.domain.dto.response.UserResponseDto;
import com.layby.domain.entity.User;
import com.layby.domain.repository.UserRepository;
import com.layby.web.exception.AES256Exception;
import com.layby.web.service.UserService;
import com.layby.web.util.AES256;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public UserResponseDto referUser(Long userId) {
        User user = userRepository.findByUserId(userId);

        String encodedPhoneNumber = user.getPhoneNumber();
        String phoneNumber = null;

        log.info("encodedPhoneNumber = {}", encodedPhoneNumber);

        try {
            phoneNumber = personalDataEncoder.decode(encodedPhoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AES256Exception(ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        }

        return new UserResponseDto(phoneNumber);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    @Override
    @Transactional
    public ResponseEntity<ResponseDto> updatePhoneNumber(Long userId, PhoneNumberUpdateRequestDto dto) {
        User foundUser = userRepository.findByUserId(userId);

        String phoneNumber = dto.getPhoneNumber();
        String encodedPhoneNumber = null;

        try {
            encodedPhoneNumber  = personalDataEncoder.encode(phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AES256Exception(ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        }

        foundUser.updatePhoneNumber(encodedPhoneNumber);

        return ResponseDto.success();
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseDto> updatePassword(Long userId, UserPasswordUpdateRequestDto dto) {
        User foundUser = userRepository.findByUserId(userId);

        String password = dto.getPassword();
        String encodedPassword = null;

        try {
            encodedPassword = personalDataEncoder.encode(password);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AES256Exception(ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        }

        foundUser.updatePassword(encodedPassword);

        return ResponseDto.success();
    }
}