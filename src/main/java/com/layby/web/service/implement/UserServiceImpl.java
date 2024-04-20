package com.layby.web.service.implement;

import com.layby.domain.dto.UserDto;
import com.layby.domain.dto.request.auth.SignInRequestDto;
import com.layby.domain.dto.request.auth.SignUpRequestDto;
import com.layby.domain.entity.AuthorityEntity;
import com.layby.domain.entity.UserEntity;
import com.layby.domain.repository.UserRepository;
import com.layby.web.exception.DuplicateMemberException;
import com.layby.web.exception.NotFoundMemberException;
import com.layby.web.exception.ValidationFailedException;
import com.layby.web.service.UserService;
import com.layby.web.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDto signup(SignUpRequestDto dto) {
        if (userRepository.findOneWithAuthoritiesByUsername(dto.getUsername()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        AuthorityEntity authorityEntity = AuthorityEntity.builder()
//                .authorityName("ROLE_USER")
                .build();

        UserEntity userEntity = UserEntity.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .authorities(Collections.singleton(authorityEntity))
                .build();

        authorityEntity.setUserEntity(userEntity);

        return UserDto.userToDto(userRepository.save(userEntity));
    }

    @Override
    @Transactional
    public void signIn(SignInRequestDto dto) {
        UserEntity user = userRepository.findByUsername(dto.getUsername());
        if (user == null) throw new NotFoundMemberException("존재하지 않는 계정입니다.");
        boolean isValid = passwordEncoder.matches(dto.getPassword(), user.getPassword());
        if (!isValid) throw new ValidationFailedException("비밀번호가 일치하지 않습니다.");
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserWithAuthorities(String username) {
        return UserDto.userToDto(userRepository.findOneWithAuthoritiesByUsername(username).orElse(null));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getMyUserWithAuthorities() {
        return UserDto.userToDto(
                SecurityUtil.getCurrentUsername()
                        .flatMap(userRepository::findOneWithAuthoritiesByUsername)
                        .orElseThrow(() -> new NotFoundMemberException("Member not found"))
        );
    }
}