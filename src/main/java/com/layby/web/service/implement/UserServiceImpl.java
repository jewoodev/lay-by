package com.layby.web.service.implement;


import com.layby.domain.dto.request.PhoneNumberUpdateRequestDto;
import com.layby.domain.dto.request.UserPasswordUpdateRequestDto;
import com.layby.domain.dto.response.ResponseDto;
import com.layby.domain.dto.response.UserResponseDto;
import com.layby.domain.entity.UserEntity;
import com.layby.domain.repository.UserRepository;
import com.layby.web.exception.AES256Exception;
import com.layby.web.service.UserService;
import com.layby.web.util.AES256;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AES256 personalDataEncoder;


    @Override
    public UserEntity findByUserId(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public UserResponseDto referUser(Long userId) {
        UserEntity userEntity = userRepository.findById(userId).orElse(null);

        String password = null;
        String phoneNumber = null;

        try {
            password = personalDataEncoder.decode(userEntity.getPassword());
            phoneNumber = personalDataEncoder.decode(userEntity.getPhoneNumber());
        } catch (Exception e) {
            e.printStackTrace();
            throw new AES256Exception("서버에서 문제가 발생되었습니다. 서버 관리자에게 문의해주세요.");
        }

        return new UserResponseDto(password, phoneNumber);
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    @Override
    @Transactional
    public ResponseEntity<ResponseDto> updatePhoneNumber(Long userId, PhoneNumberUpdateRequestDto dto) {
        UserEntity foundUserEntity = userRepository.findById(userId).orElse(null);

        String phoneNumber = dto.getPhoneNumber();
        String encodedPhoneNumber = null;

        try {
            encodedPhoneNumber  = personalDataEncoder.encode(phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AES256Exception("서버에서 문제가 발생되었습니다. 서버 관리자에게 문의해주세요.");
        }

        foundUserEntity.updatePhoneNumber(encodedPhoneNumber);

        return ResponseDto.successful();
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseDto> updatePassword(Long userId, UserPasswordUpdateRequestDto dto) {
        UserEntity foundUserEntity = userRepository.findById(userId).orElse(null);

        String password = dto.getPassword();
        String encodedPassword = null;

        try {
            encodedPassword = personalDataEncoder.encode(password);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AES256Exception("서버에서 문제가 발생되었습니다. 서버 관리자에게 문의해주세요.");
        }

        foundUserEntity.updatePassword(password);

        return ResponseDto.successful();
    }
}