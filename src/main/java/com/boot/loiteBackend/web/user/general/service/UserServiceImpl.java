package com.boot.loiteBackend.web.user.general.service;

import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.global.security.CustomUserDetails;
import com.boot.loiteBackend.web.social.service.SocialLinkService;
import com.boot.loiteBackend.web.user.general.dto.UserCreateRequestDto;
import com.boot.loiteBackend.web.user.general.entity.UserEntity;
import com.boot.loiteBackend.web.user.general.error.UserErrorCode;
import com.boot.loiteBackend.web.user.general.mapper.UserMapper;
import com.boot.loiteBackend.web.user.general.repository.UserRepository;
import com.boot.loiteBackend.web.user.role.entity.UserRoleEntity;
import com.boot.loiteBackend.web.user.role.error.UserRoleErrorCode;
import com.boot.loiteBackend.web.user.role.repository.UserRoleRepository;
import com.boot.loiteBackend.web.user.status.entity.UserStatusEntity;
import com.boot.loiteBackend.web.user.status.error.UserStatusErrorCode;
import com.boot.loiteBackend.web.user.status.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SocialLinkService socialLinkService;
    private final UserRoleRepository userRoleCodeRepository;
    private final UserStatusRepository userStatusCodeRepository;

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

        UserRoleEntity role = userRoleCodeRepository.findById("USER")
                .orElseThrow(() -> new CustomException(UserRoleErrorCode.ROLE_NOT_FOUND));
        user.setUserRole(role);

        UserStatusEntity status = userStatusCodeRepository.findById("ACTIVE")
                .orElseThrow(() -> new CustomException(UserStatusErrorCode.STATUS_NOT_FOUND));
        user.setUserStatus(status);

        user.setUserRegisterType("EMAIL");

        return userRepository.save(user).getUserId();
    }

    @Override
    @Transactional
    public void withdraw(CustomUserDetails loginUser, String accessToken) {
        UserEntity user = userRepository.findById(loginUser.getUserId())
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        String provider = loginUser.getUserRegisterType();
        socialLinkService.unlinkAccount(provider, loginUser, accessToken);

        userRepository.delete(user);
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