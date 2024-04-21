package com.layby.web.service.implement;


import com.layby.domain.dto.request.PhoneNumberUpdateRequestDto;
import com.layby.domain.dto.request.UserPasswordUpdateRequestDto;
import com.layby.domain.dto.response.PhoneNumberUpdateResponseDto;
import com.layby.domain.dto.response.UserPasswordUpdateResponseDto;
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
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public UserResponseDto referUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);

        String encodedPhoneNumber = user.getPhoneNumber();
        String phoneNumber = null;

        log.info("encodedPhoneNumber = {}", encodedPhoneNumber);

        try {
            phoneNumber = personalDataEncoder.decode(encodedPhoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AES256Exception("서버에서 문제가 발생되었습니다. 서버 관리자에게 문의해주세요.");
        }

        return new UserResponseDto(phoneNumber);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    @Override
    @Transactional
    public ResponseEntity<PhoneNumberUpdateResponseDto> updatePhoneNumber(Long userId, PhoneNumberUpdateRequestDto dto) {
        User foundUser = userRepository.findById(userId).orElse(null);

        String phoneNumber = dto.getPhoneNumber();
        String encodedPhoneNumber = null;

        try {
            encodedPhoneNumber  = personalDataEncoder.encode(phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AES256Exception("서버에서 문제가 발생되었습니다. 서버 관리자에게 문의해주세요.");
        }

        foundUser.updatePhoneNumber(encodedPhoneNumber);

        return PhoneNumberUpdateResponseDto.success();
    }

    @Override
    @Transactional
    public ResponseEntity<UserPasswordUpdateResponseDto> updatePassword(Long userId, UserPasswordUpdateRequestDto dto) {
        User foundUser = userRepository.findById(userId).orElse(null);

        String password = dto.getPassword();
        String encodedPassword = null;

        try {
            encodedPassword = personalDataEncoder.encode(password);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AES256Exception("서버에서 문제가 발생되었습니다. 서버 관리자에게 문의해주세요.");
        }

        foundUser.updatePassword(encodedPassword);

        return UserPasswordUpdateResponseDto.success();
    }
}