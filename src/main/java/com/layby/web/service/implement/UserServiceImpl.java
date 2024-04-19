package com.layby.web.service.implement;

import com.layby.domain.dto.UserDto;
import com.layby.domain.entity.AuthorityEntity;
import com.layby.domain.entity.UserEntity;
import com.layby.domain.repository.UserRepository;
import com.layby.web.exception.DuplicateMemberException;
import com.layby.web.exception.NotFoundMemberException;
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
    public UserDto signup(UserDto userDto) {
        if (userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }


        AuthorityEntity authorityEntity = AuthorityEntity.builder()
                .authorityName("ROLE_USER")
                .build();

        UserEntity userEntity = UserEntity.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .authorities(Collections.singleton(authorityEntity))
                .build();

        authorityEntity.setUserEntity(userEntity);

        return UserDto.userToDto(userRepository.save(userEntity));
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