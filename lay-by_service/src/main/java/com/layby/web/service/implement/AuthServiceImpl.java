package com.layby.web.service.implement;

import com.layby.domain.common.RedisDao;
import com.layby.domain.common.Role;
import com.layby.domain.dto.request.auth.*;
import com.layby.domain.dto.response.ResponseDto;
import com.layby.domain.dto.response.auth.*;
import com.layby.domain.entity.MailCertification;
import com.layby.domain.entity.User;
import com.layby.domain.repository.CertificationRepository;
import com.layby.domain.repository.UserRepository;
import com.layby.web.exception.*;
import com.layby.web.jwt.JwtProvider;
import com.layby.web.provider.EmailProvider;
import com.layby.web.service.AuthService;
import com.layby.web.util.AES256;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

import static com.layby.domain.common.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final CertificationRepository certificationRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AES256 personalDataEncoder;

    private final JwtProvider jwtProvider;
    private final EmailProvider emailProvider;

    private final RedisDao redisDao;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto> usernameCheck(UsernameCheckRequestDto dto) {

        try {
            String username = dto.getUsername();
            boolean isExist = userRepository.existsByUsername(username);
            if (isExist) throw new DuplicatedUsernameException(DUPLICATED_USERNAME.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseErrorException(DATABASE_ERROR.getMessage());
        }

        return ResponseDto.success();
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseDto> emailCertification(EmailCertificationRequestDto dto) {

        String username = null;
        String encodedUsername = null;
        String email = null;
        String encodedEmail = null;

        try {
            username = dto.getUsername();
            encodedUsername = personalDataEncoder.encode(username);
            email = dto.getEmail();
            encodedEmail = personalDataEncoder.encode(email);

        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorException(INTERNAL_SERVER_ERROR.getMessage());
        }

        try {
            boolean isExist = userRepository.existsByUsername(encodedUsername);
            if (!isExist) throw new MailFailedException(MAIL_FAIL.getMessage());

            String certificationNumber = getCertificationNumber();

            boolean isSuccessed = emailProvider.sendCertificationMail(email, certificationNumber);
            if (!isSuccessed) throw new MailFailedException(MAIL_FAIL.getMessage());

            boolean isExistCF = certificationRepository.existsByUsername(username);
            if (isExistCF) certificationRepository.deleteAllByUsername(username);

            MailCertification mailCertification = new MailCertification(username, email, certificationNumber);
            certificationRepository.save(mailCertification);

        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseErrorException(DATABASE_ERROR.getMessage());
        }

        return ResponseDto.success();
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseDto> checkCertification(CheckCertificationRequestDto dto) {

        String username = dto.getUsername();
        String email = dto.getEmail();
        String certificationNumber = dto.getCertificationNumber();

        boolean isExist = certificationRepository.existsByUsername(username);
        if (!isExist) throw new CertificationFailedException(CERTIFICATION_FAIL.getMessage());

        MailCertification mailCertification = certificationRepository.findByUsername(username);
        boolean isMatched = mailCertification.getEmail().equals(email) &&
                mailCertification.getCertificationNumber().equals(certificationNumber);
        if (!isMatched) throw new CertificationFailedException(CERTIFICATION_FAIL.getMessage());

        String encodedUsername = null;

        try {
            encodedUsername = personalDataEncoder.encode(username);

        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorException(INTERNAL_SERVER_ERROR.getMessage());
        }

        try {
            User user = userRepository.findByUsername(encodedUsername);

            user.updateAfterCertification(Role.USER, LocalDateTime.now());

            certificationRepository.deleteAllByUsername(username);

        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseErrorException(DATABASE_ERROR.getMessage());
        }

        return ResponseDto.success();
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseDto> signUp(SignUpRequestDto dto) {

        try {
            String username = dto.getUsername();
            boolean isExist = userRepository.existsByUsername(username);
            if (isExist) throw new DuplicatedUsernameException(DUPLICATED_USERNAME.getMessage());
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

            User user = new User(dto);
            userRepository.save(user);

        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorException(INTERNAL_SERVER_ERROR.getMessage());
        }

        return ResponseDto.success();
    }

    @Override
    @Transactional
    public ResponseEntity<SignInResponseDto> signIn(SignInRequestDto dto) {

        String token = null;
        String encodedUsername = null;

        try {
            String username = dto.getUsername();
            encodedUsername = personalDataEncoder.encode(username);
            User user = userRepository.findByUsername(encodedUsername);
            if (user == null) throw new SignInFailedException(SIGN_IN_FAIL.getMessage());

            String password = dto.getPassword();
            String encodedPassword = user.getPassword();
            boolean isMatched = passwordEncoder.matches(password, encodedPassword);
            if (!isMatched) throw new SignInFailedException(SIGN_IN_FAIL.getMessage());

            token = jwtProvider.createToken(username);

        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorException(INTERNAL_SERVER_ERROR.getMessage());
        }

        redisDao.setValue("JWT_TOKEN:" + encodedUsername, token, Duration.ofMillis(jwtProvider.getTokenValidTime()));
        SignInResponseDto signInResponseDto = new SignInResponseDto(token);

        return ResponseEntity.status(HttpStatus.OK).body(signInResponseDto);
    }

    public ResponseEntity<ResponseDto> logout(Authentication authentication) {
        String username = authentication.getPrincipal().toString();
        String encodedUsername = null;

        try {
            encodedUsername = personalDataEncoder.encode(username);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorException(INTERNAL_SERVER_ERROR.getMessage());
        }

        String key = "JWT_TOKEN:" + encodedUsername;
        String token = redisDao.getValue(key);
        if (token != null) {
            redisDao.deleteValue(key);
        }

        return ResponseDto.success();
    }

    private String getCertificationNumber() {

        StringBuilder certificationNumber = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            certificationNumber.append((int) (Math.random() * 10));
        }

        return certificationNumber.toString();
    }
}
