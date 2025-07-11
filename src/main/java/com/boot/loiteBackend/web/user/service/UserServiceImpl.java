package com.boot.loiteBackend.web.user.service;

import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.web.user.dto.UserCreateRequestDto;
import com.boot.loiteBackend.web.user.entity.UserEntity;
import com.boot.loiteBackend.web.user.error.UserErrorCode;
import com.boot.loiteBackend.web.user.mapper.UserMapper;
import com.boot.loiteBackend.web.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
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

        if (!dto.getUserPassword().equals(dto.getUserPasswordCheck())) {
            throw new CustomException(UserErrorCode.PASSWORD_MISMATCH);
        }

        LocalDate birthdate;
        try {
            birthdate = LocalDate.parse(dto.getUserBirthdate());  // "yyyy-MM-dd" 형식이어야 함
        } catch (Exception e) {
            throw new CustomException(UserErrorCode.INVALID_BIRTHDATE_FORMAT);
        }

        UserEntity user = userMapper.toEntity(dto);
        user.setUserPassword(dto.getUserPassword());
        user.setUserBirthdate(birthdate);
        user.setEmailVerified(false);
        user.setUserRole("USER");
        user.setUserStatus("ACTIVE");
        user.setUserRegisterType("EMAIL");

        return userRepository.save(user).getUserId();
    }

    @Override
    public void withdraw() {
        // 인증된 사용자 조회 (SecurityContext 기반)
        UserEntity currentUser = getCurrentUser();

        // 소프트 삭제 방식 권장
        currentUser.setUserStatus("DELETED");
        userRepository.save(currentUser);

        // 만약 물리 삭제를 원한다면 아래 코드 사용
        userRepository.delete(currentUser);
    }

    private UserEntity getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByUserEmail(email)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
    }

    @Override
    public void withdrawById(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        // 소프트 삭제
        user.setUserStatus("DELETED");
        userRepository.save(user);

        // 물리 삭제를 원한다면 아래 한 줄로 대체 가능
        userRepository.delete(user);
    }
}
