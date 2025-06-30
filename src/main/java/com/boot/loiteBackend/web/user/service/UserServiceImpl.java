package com.boot.loiteBackend.web.user.service;

import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.web.user.dto.UserCreateRequestDto;
import com.boot.loiteBackend.web.user.entity.UserEntity;
import com.boot.loiteBackend.web.user.error.UserErrorCode;
import com.boot.loiteBackend.web.user.mapper.UserMapper;
import com.boot.loiteBackend.web.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public Long signup(UserCreateRequestDto dto) {

        if (userRepository.existsByUserEmail(dto.getUserEmail())) {
            throw new CustomException(UserErrorCode.EMAIL_DUPLICATED);
        }

        UserEntity user = userMapper.toEntity(dto);

        user.setUserPassword(dto.getUserPassword());
        user.setUserBirthdate(LocalDate.parse(dto.getUserBirthdate()));
        user.setEmailVerified(false);
        user.setRole("USER");
        user.setUserStatus("ACTIVE");

        return userRepository.save(user).getUserId();
    }
}
