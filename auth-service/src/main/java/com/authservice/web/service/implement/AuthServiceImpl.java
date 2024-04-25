package com.authservice.web.service.implement;

import com.authservice.domain.common.RedisDao;
import com.authservice.domain.dto.request.*;
import com.authservice.domain.dto.response.ResponseDto;
import com.authservice.domain.dto.response.SignInResponseDto;
import com.authservice.domain.entity.User;
import com.authservice.domain.repository.UserRepository;
import com.authservice.web.exception.*;
import com.authservice.web.jwt.JwtProvider;
import com.authservice.web.provider.EmailProvider;
import com.authservice.web.service.AuthService;
import com.authservice.web.util.AES256;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

import static com.authservice.domain.common.ErrorCode.*;


@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

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

            boolean isExistCF = redisDao.hasKey(encodedEmail);
            if (isExistCF) redisDao.deleteValue(encodedEmail);

            redisDao.setValue(encodedEmail, certificationNumber);

        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseErrorException(DATABASE_ERROR.getMessage());
        }

        return ResponseDto.success();
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseDto> checkCertification(
            CheckCertificationRequestDto dto
    ) {

        String email = dto.getEmail();
        String encodedEmail = null;
        String certificationNumber = dto.getCertificationNumber();

        try {
            encodedEmail = personalDataEncoder.encode(email);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorException(INTERNAL_SERVER_ERROR.getMessage());
        }

        boolean isExist = redisDao.hasKey(encodedEmail);
        if (!isExist) throw new CertificationFailedException(CERTIFICATION_FAIL.getMessage());

        String value = redisDao.getValue(encodedEmail);
        boolean isMatched = certificationNumber.equals(value);
        if (!isMatched) throw new CertificationFailedException(CERTIFICATION_FAIL.getMessage());

        String username = dto.getUsername();
        String encodedUsername = null;

        try {
            encodedUsername = personalDataEncoder.encode(username);

        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorException(INTERNAL_SERVER_ERROR.getMessage());
        }

        try {
            User user = userRepository.findByUsername(encodedUsername);
            user.afterCertification();

            redisDao.deleteValue(encodedEmail);

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

            User user = User.forSignIn(dto);
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
