package com.authservice.web.jwt;

import com.authservice.domain.entity.User;
import com.authservice.domain.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider implements InitializingBean {

    private final String secretKey;
    private Key key;

    private static final String AUTHORITIES_KEY = "auth";

    @Getter
    private final long tokenValidTime = 60 * 60 * 1000L;

    private UserRepository userRepository;

    public JwtProvider(
            @Value("${jwt.secret-key}") String secretKey,
            UserRepository userRepository
    ) {
        this.secretKey = secretKey;
        this.userRepository = userRepository;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(String username) {

        User user = userRepository.findByUsername(username);

        Date expiredDate = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));

        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS512)
                .setSubject(username).setIssuedAt(new Date())
                .claim(AUTHORITIES_KEY, user.getRole().name())
                .setExpiration(expiredDate)
                .compact();
    }

    public String validate(String jwt) {

        String subject = null;

        try {
            subject = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody()
                    .getSubject();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return subject;
    }
}