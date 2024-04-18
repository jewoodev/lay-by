package com.layby.web.service.implement;

import com.layby.domain.dto.UserDto;
import com.layby.domain.entity.Authority;
import com.layby.domain.entity.User;
import com.layby.domain.repository.UserRepository;
import com.layby.web.exception.DuplicateMemberException;
import com.layby.web.exception.NotFoundMemberException;
import com.layby.web.service.UserService;
import com.layby.web.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserDto signup(UserDto userDto) {
        if (userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }


        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        authority.setUser(user);

        return UserDto.userToDto(userRepository.save(user));
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