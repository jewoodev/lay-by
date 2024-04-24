package com.authservice.domain.entity;

import com.authservice.domain.common.Role;
import com.authservice.domain.dto.request.SignUpRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

import static com.authservice.domain.common.Role.*;

@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
@Entity(name = "user")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "username", length = 50, unique = true)
    private String username;

    @Column(name = "email", length = 100, unique = true)
    private String email;

    @Column(name = "email_verified_at")
    private LocalDateTime emailVerifiedAt;

    @Column(name = "password")
    private String password;

    @Column(name = "phone_number", length = 50)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    //== 생성자 메서드 ==//
    /** sign up request dto를 받는 생성자 메서드 **/
    public static User forSignIn(SignUpRequestDto dto) {
        return User.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();
    }

    //== 비즈니스 로직 ==//
    /** 이메일 인증 후 역할 부여와 인증 날짜 저장 **/
    public void afterCertification() {
        this.role = USER;
        this.emailVerifiedAt = LocalDateTime.now();
    }
}