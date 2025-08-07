package com.boot.loiteBackend.web.user.general.service;

import com.boot.loiteBackend.config.security.CustomUserDetails;
import com.boot.loiteBackend.web.user.general.dto.*;
import com.boot.loiteBackend.domain.user.general.entity.UserEntity;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    Long signup(UserCreateRequestDto dto);

    void withdraw(CustomUserDetails loginUser, String token,  HttpServletResponse response);

    void withdrawById(Long userId);

    boolean isEmailDuplicated(String userEmail);

    void updatePassword(@Valid UpdatePasswordRequestDto dto);

    String findUserId(@Valid FindUserIdRequestDto dto);

    void validateUserForPasswordReset(@Valid ResetPasswordRequestDto dto);

    boolean isPhoneDuplicated(String phone);

    void updateUserInfo(CustomUserDetails loginUser, UserUpdateInfoRequestDto request);

    void updateMarketingAgreement(CustomUserDetails loginUser, UserMarketingAgreementRequestDto request);

    UserInfoDto getUserInfo(CustomUserDetails loginUser);

    boolean check(CustomUserDetails user, String password);

    void changePassword(CustomUserDetails loginUser, @Valid UpdatePasswordRequestDto request);

    UserEntity findUserEntity(@Valid FindUserIdRequestDto dto);
}
