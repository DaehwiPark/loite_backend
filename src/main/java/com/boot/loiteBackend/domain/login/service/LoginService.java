package com.boot.loiteBackend.domain.login.service;

import com.boot.loiteBackend.global.security.CustomUserDetails;
import com.boot.loiteBackend.domain.login.dto.LoginRequestDto;
import com.boot.loiteBackend.domain.login.dto.LoginResponseDto;
import com.boot.loiteBackend.web.user.dto.UserSummaryDto;
import jakarta.servlet.http.HttpServletResponse;

public interface LoginService {

    LoginResponseDto login(LoginRequestDto dto, HttpServletResponse response);

    void logout(CustomUserDetails userDetails, HttpServletResponse response);

    UserSummaryDto myInfo(CustomUserDetails user);
}
