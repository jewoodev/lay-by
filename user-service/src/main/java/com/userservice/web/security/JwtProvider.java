package com.userservice.web.security;

import com.userservice.domain.common.ErrorCode;
import com.userservice.domain.entity.User;
import com.userservice.domain.repository.UserRepository;
import com.userservice.web.exception.InternalServerErrorException;
import com.userservice.web.util.PersonalDataEncoder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static com.userservice.domain.common.ErrorCode.*;

@Slf4j
@Component
public class JwtProvider implements InitializingBean {

    private final String secretKey;
    private final UserRepository userRepository;
    private final PersonalDataEncoder personalDataEncoder;
    private Key key;

    public JwtProvider(
            @Value("${jwt.secret-key}") String secretKey,
            UserRepository userRepository, PersonalDataEncoder personalDataEncoder
    ) {
        this.secretKey = secretKey;
        this.userRepository = userRepository;
        this.personalDataEncoder = personalDataEncoder;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(String username) {

        String encodedUsername = null;

        try {
            encodedUsername = personalDataEncoder.encode(username);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorException(INTERNAL_SERVER_ERROR.getMessage());
        }

        User user = userRepository.findByUsername(encodedUsername);

        Date expiredDate = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));

        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS512)
                .setSubject(user.getUuid()).setIssuedAt(new Date())
                .claim("auth", user.getRole().name())
                .setExpiration(expiredDate)
                .compact();
    }
}
