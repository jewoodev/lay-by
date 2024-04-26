package com.userservice.web.service.implement;

import com.userservice.domain.dto.ResponseDto;
import com.userservice.domain.vo.PhoneNumberUpdateRequest;
import com.userservice.domain.vo.UserPasswordUpdateRequest;
import com.userservice.domain.dto.UserDto;
import com.userservice.domain.entity.User;
import com.userservice.domain.repository.UserRepository;
import com.userservice.web.exception.AES256Exception;
import com.userservice.web.service.UserService;
import com.userservice.web.util.PersonalDataEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.userservice.domain.common.ErrorCode.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PersonalDataEncoder personalDataEncoder;

    @Override
    public User findByUserId(Long userId) {
        return userRepository.findByUserId(userId);
    }

    @Override
    public ResponseEntity<UserDto> referUser(Long userId) {
        User user = userRepository.findByUserId(userId);

        String encodedUsername = user.getUsername();
        String encodedEmail = user.getEmail();
        String encodedPhoneNumber = user.getPhoneNumber();
        String username = null;
        String email = null;
        String phoneNumber = null;

        log.info("encodedPhoneNumber = {}", encodedPhoneNumber);

        try {
            username = personalDataEncoder.decode(encodedUsername);
            email = personalDataEncoder.decode(encodedEmail);
            phoneNumber = personalDataEncoder.decode(encodedPhoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AES256Exception(INTERNAL_SERVER_ERROR.getMessage());
        }

        UserDto responseBody = UserDto.builder()
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
    public ResponseEntity<ResponseDto> updatePhoneNumber(Long userId, PhoneNumberUpdateRequest dto) {
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
    @Transactional
    public ResponseEntity<ResponseDto> updatePassword(Long userId, UserPasswordUpdateRequest dto) {
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
    @Transactional
    public ResponseEntity<ResponseDto> updateRoleAfterEmailCF(Long userId) {
        User user = userRepository.findByUserId(userId);

        user.afterCertification();

        return ResponseDto.success();
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Long save(User user) {
        User saved = userRepository.save(user);
        return saved.getUserId();
    }
}