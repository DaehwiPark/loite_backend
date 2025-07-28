package com.boot.loiteBackend.web.user.general.service;

import com.boot.loiteBackend.global.response.ApiResponse;
import com.boot.loiteBackend.global.security.CustomUserDetails;
import com.boot.loiteBackend.web.user.general.dto.FindUserIdRequestDto;
import com.boot.loiteBackend.web.user.general.dto.ResetPasswordRequestDto;
import com.boot.loiteBackend.web.user.general.dto.UpdatePasswordRequestDto;
import com.boot.loiteBackend.web.user.general.dto.UserCreateRequestDto;
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
}
