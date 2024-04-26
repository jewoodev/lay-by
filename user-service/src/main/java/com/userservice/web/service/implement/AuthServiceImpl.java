package com.userservice.web.service.implement;

import com.userservice.domain.common.RedisDao;
import com.userservice.domain.dto.ResponseDto;
import com.userservice.domain.dto.auth.TokenDto;
import com.userservice.domain.entity.User;
import com.userservice.domain.repository.UserRepository;
import com.userservice.domain.vo.auth.*;
import com.userservice.web.exception.*;
import com.userservice.web.provider.EmailProvider;
import com.userservice.web.security.JwtProvider;
import com.userservice.web.service.UserService;
import com.userservice.web.util.PersonalDataEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

import com.userservice.web.service.AuthService;

import static com.userservice.domain.common.ErrorCode.*;
import static com.userservice.domain.common.ErrorCode.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final PersonalDataEncoder personalDataEncoder;

    private final JwtProvider jwtProvider;
    private final EmailProvider emailProvider;

    private final RedisDao redisDao;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto> usernameCheck(UsernameCheckRequest dto) {

        try {
            String username = dto.getUsername();
            boolean isExist = userService.existsByUsername(username);
            if (isExist) throw new DuplicatedUsernameException(DUPLICATED_USERNAME.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseErrorException(DATABASE_ERROR.getMessage());
        }

        return ResponseDto.success();
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseDto> emailCertification(EmailCertificationRequest dto) {

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
            boolean isExist = userService.existsByUsername(encodedUsername);
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
            CheckCertificationRequest dto
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
            User user = userService.findByUsername(encodedUsername);
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
    public ResponseEntity<ResponseDto> signUp(SignUpRequest dto) {

        try {
            String username = dto.getUsername();
            boolean isExist = userService.existsByUsername(username);
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
            userService.save(user);

        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorException(INTERNAL_SERVER_ERROR.getMessage());
        }

        return ResponseDto.success();
    }

    @Override
    @Transactional
    public TokenDto signIn(SignInRequest dto) {

        String token = null;
        String encodedUsername = null;

        try {
            String username = dto.getUsername();
            encodedUsername = personalDataEncoder.encode(username);
            User user = userService.findByUsername(encodedUsername);
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

        redisDao.setValue("JWT_TOKEN:" + encodedUsername, token, Duration.ofMillis(60 * 60 * 1000L));
        TokenDto tokenDto = new TokenDto(token);

        return tokenDto;
    }

    public ResponseEntity<ResponseDto> logout(Long userId) {
        User user = userService.findByUserId(userId);
        String encodedUsername = null;

        try {
            encodedUsername = personalDataEncoder.encode(user.getUsername());
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
