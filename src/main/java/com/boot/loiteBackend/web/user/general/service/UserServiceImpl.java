package com.boot.loiteBackend.web.user.general.service;

import com.boot.loiteBackend.domain.token.service.TokenService;
import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.global.security.CustomUserDetails;
import com.boot.loiteBackend.global.security.jwt.JwtCookieUtil;
import com.boot.loiteBackend.global.security.jwt.JwtTokenProvider;
import com.boot.loiteBackend.web.social.service.SocialLinkService;
import com.boot.loiteBackend.web.user.general.dto.FindUserIdRequestDto;
import com.boot.loiteBackend.web.user.general.dto.ResetPasswordRequestDto;
import com.boot.loiteBackend.web.user.general.dto.UpdatePasswordRequestDto;
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
import jakarta.servlet.http.HttpServletResponse;
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
    private final TokenService tokenService;
    private final JwtCookieUtil jwtCookieUtil;

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
    public void withdraw(CustomUserDetails loginUser, String accessToken, HttpServletResponse response) {

        UserEntity user = userRepository.findById(loginUser.getUserId())
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        String provider = loginUser.getUserRegisterType();

        if (!"email".equalsIgnoreCase(provider)) {
            socialLinkService.unlinkAccount(provider, loginUser, accessToken);
        }

        // redis 에서 token 저장 정보 삭제
        tokenService.deleteRefreshToken(String.valueOf(user.getUserId()));
        // 브라우저에서 AccessToken 삭제
        jwtCookieUtil.deleteAccessTokenCookie(response);
        // 브라우저에서 RefreshToken 삭제
        jwtCookieUtil.deleteRefreshTokenCookie(response);
        // db 에서 회원 정보 삭제
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

    @Override
    public String findUserId(FindUserIdRequestDto dto) {
        UserEntity user = userRepository.findByUserNameAndUserPhone(dto.getName(), dto.getPhone())
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
        return user.getUserEmail();
    }

    @Override
    public void validateUserForPasswordReset(ResetPasswordRequestDto dto) {
        boolean exists = userRepository.existsByUserEmailAndUserNameAndUserPhone(
                dto.getEmail(), dto.getName(), dto.getPhone());

        if (!exists) {
            throw new CustomException(UserErrorCode.USER_NOT_FOUND);
        }
    }
    
    @Override
    public void updatePassword(UpdatePasswordRequestDto dto) {
        UserEntity user = userRepository.findByUserEmail(dto.getEmail())
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        user.setUserPassword(dto.getNewPassword());
        userRepository.save(user);
    }
}