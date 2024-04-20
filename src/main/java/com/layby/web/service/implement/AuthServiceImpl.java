package com.layby.web.service.implement;

import com.layby.domain.common.Role;
import com.layby.domain.dto.request.auth.*;
import com.layby.domain.dto.response.ResponseDto;
import com.layby.domain.dto.response.auth.*;
import com.layby.domain.entity.CertificationEntity;
import com.layby.domain.entity.UserEntity;
import com.layby.domain.repository.CertificationRepository;
import com.layby.domain.repository.UserRepository;
import com.layby.web.jwt.JwtProvider;
import com.layby.web.provider.EmailProvider;
import com.layby.web.service.AuthService;
import com.layby.web.util.AES256;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final CertificationRepository certificationRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AES256 personalDataEncoder;

    private final JwtProvider jwtProvider;
    private final EmailProvider emailProvider;

    @Override
    public ResponseEntity<? super UsernameCheckReponseDto> usernameCheck(UsernameCheckRequestDto dto) {

        try {
            String username = dto.getUsername();
            boolean isExist = userRepository.existsByUsername(username);
            if (isExist) return UsernameCheckReponseDto.duplicatedUsername();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return UsernameCheckReponseDto.success();
    }

    @Override
    public ResponseEntity<? super EmailCertificationResponseDto> emailCertification(EmailCertificationRequestDto dto) {
        try {
            String encodedUsername = dto.getUsername();
            String username = personalDataEncoder.decode(encodedUsername);
            String encodedEmail = dto.getEmail();
            String email = personalDataEncoder.decode(encodedEmail);

            boolean isExist = userRepository.existsByUsername(username);
            if (!isExist) return EmailCertificationResponseDto.mailSendFail();

            String certificationNumber = getCertificationNumber();

            boolean isSuccessed = emailProvider.sendCertificationMail(email, certificationNumber);
            if (!isSuccessed) return EmailCertificationResponseDto.mailSendFail();

            boolean isExistCF = certificationRepository.existsByUsername(username);
            if (isExistCF) certificationRepository.deleteAllByUsername(username);

            CertificationEntity certificationEntity = new CertificationEntity(username, email, certificationNumber);
            certificationRepository.save(certificationEntity);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return EmailCertificationResponseDto.success();
    }

    @Override
    @Transactional
    public ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto dto) {
        try {
            String username = dto.getUsername();
            String email = dto.getEmail();
            String certificationNumber = dto.getCertificationNumber();

            boolean isExist = certificationRepository.existsByUsername(username);
            if (!isExist) return CheckCertificationResponseDto.certificationFail();

            CertificationEntity certificationEntity = certificationRepository.findByUsername(username);
            boolean isMatched = certificationEntity.getEmail().equals(email) &&
                    certificationEntity.getCertificationNumber().equals(certificationNumber);
            if (!isMatched) return CheckCertificationResponseDto.certificationFail();

            UserEntity userEntity = userRepository.findByUsername(username);

            userEntity.updateAfterCertification(Role.USER, LocalDateTime.now());

            certificationRepository.deleteAllByUsername(username);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return CheckCertificationResponseDto.success();
    }

    @Override
    public ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto) {

        try {
            String username = dto.getUsername();
            boolean isExist = userRepository.existsByUsername(username);
            if (isExist) return SignUpResponseDto.duplicatedUsername();
            String encodedUsername = personalDataEncoder.encode(username);
            dto.setUsername(encodedUsername);

            String password = dto.getPassword();
            String encodedPassword= passwordEncoder.encode(password);
            dto.setPassword(encodedPassword);

            String email = dto.getEmail();
            String encodedEmail = personalDataEncoder.encode(email);
            dto.setEmail(encodedEmail);

            String phoneNumber = dto.getPhoneNumber();
            String encodedPhoneNumber = personalDataEncoder.encode(phoneNumber);
            dto.setPhoneNumber(encodedPhoneNumber);

            UserEntity userEntity = new UserEntity(dto);
            userRepository.save(userEntity);

        } catch (Exception e) {
            e.printStackTrace();
            return SignUpResponseDto.internalError();
        }
        return SignUpResponseDto.success();
    }

    @Override
    public ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto) {

        String token = null;

        try {
            String encodedUsername = dto.getUsername();
            String username = personalDataEncoder.decode(encodedUsername);
            UserEntity userEntity = userRepository.findByUsername(username);
            if (userEntity == null) return SignInResponseDto.signInFail();

            String password = dto.getPassword();
            String encodedPassword = userEntity.getPassword();
            boolean isMatched = passwordEncoder.matches(password, encodedPassword);
            if (!isMatched) return SignInResponseDto.signInFail();

            token = jwtProvider.createToken(username);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return SignInResponseDto.success(token);
    }

    private String getCertificationNumber() {

        StringBuilder certificationNumber = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            certificationNumber.append((int) (Math.random() * 10));
        }

        return certificationNumber.toString();
    }
}
