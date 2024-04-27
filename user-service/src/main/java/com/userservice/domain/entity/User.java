package com.userservice.domain.entity;

import com.userservice.domain.dto.UserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import com.userservice.domain.common.Role;

import static com.userservice.domain.common.Role.USER;

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

        @Column(name = "uuid")
    private String uuid;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    //== 생성자 메서드 ==//
    /** sign up 과정에서 dto를 받는 생성자 메서드 **/
    public static User fromDto(UserDto dto) {
        return User.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .uuid(UUID.randomUUID().toString())
                .build();
    }

    //== 비즈니스 로직 ==//
    /** 이메일 인증 후 역할 부여와 인증 날짜 저장 **/
    public void afterCertification() {
        this.role = USER;
        this.emailVerifiedAt = LocalDateTime.now();
    }
}