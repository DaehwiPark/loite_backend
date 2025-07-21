package com.boot.loiteBackend.web.user.service;

import com.boot.loiteBackend.global.code.entity.UserRoleCodeEntity;
import com.boot.loiteBackend.global.code.entity.UserStatusCodeEntity;
import com.boot.loiteBackend.global.code.repository.UserRoleCodeRepository;
import com.boot.loiteBackend.global.code.repository.UserStatusCodeRepository;
import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.global.response.ApiResponse;
import com.boot.loiteBackend.global.security.CustomUserDetails;
import com.boot.loiteBackend.web.social.entity.SocialUserEntity;
import com.boot.loiteBackend.web.social.repository.SocialUserRepository;
import com.boot.loiteBackend.web.user.dto.UserCreateRequestDto;
import com.boot.loiteBackend.web.user.entity.UserEntity;
import com.boot.loiteBackend.web.user.error.UserErrorCode;
import com.boot.loiteBackend.web.user.mapper.UserMapper;
import com.boot.loiteBackend.web.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SocialUserRepository socialUserRepository;
    private final UserRoleCodeRepository userRoleCodeRepository;
    private final UserStatusCodeRepository userStatusCodeRepository;

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
            birthdate = LocalDate.parse(dto.getUserBirthdate());
        } catch (Exception e) {
            throw new CustomException(UserErrorCode.INVALID_BIRTHDATE_FORMAT);
        }

        UserEntity user = userMapper.toEntity(dto);
        user.setUserPassword(dto.getUserPassword());
        user.setUserBirthdate(birthdate);
        user.setEmailVerified(false);

        // Set role and status as foreign key entities
        UserRoleCodeEntity role = userRoleCodeRepository.findById("USER")
                .orElseThrow(() -> new CustomException(UserErrorCode.ROLE_NOT_FOUND));
        user.setUserRole(role);

        UserStatusCodeEntity status = userStatusCodeRepository.findById("ACTIVE")
                .orElseThrow(() -> new CustomException(UserErrorCode.STATUS_NOT_FOUND));
        user.setUserStatus(status);

        user.setUserRegisterType("EMAIL");

        return userRepository.save(user).getUserId();
    }

    @Override
    public ApiResponse<String> withdraw(CustomUserDetails loginUser, String accessToken) {
        UserEntity user = userRepository.findById(loginUser.getUserId())
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        List<SocialUserEntity> socialLinks = socialUserRepository.findAllByUser(user);
        if (!socialLinks.isEmpty()) {
            return ApiResponse.error(UserErrorCode.SOCIAL_LINK_EXISTS);
        }

        userRepository.delete(user);

        return ApiResponse.ok("회원 탈퇴가 완료되었습니다.");
    }

    @Override
    public void withdrawById(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isEmailDuplicated(String userEmail) {
        return userRepository.existsByUserEmail(userEmail);
    }
}