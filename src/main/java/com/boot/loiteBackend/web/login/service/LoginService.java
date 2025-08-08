package com.boot.loiteBackend.web.login.service;

import com.boot.loiteBackend.config.security.CustomUserDetails;
import com.boot.loiteBackend.web.login.dto.LoginRequestDto;
import com.boot.loiteBackend.web.login.dto.LoginResponseDto;
import com.boot.loiteBackend.web.user.general.dto.UserSummaryDto;
import jakarta.servlet.http.HttpServletResponse;

public interface LoginService {

    LoginResponseDto login(LoginRequestDto dto, HttpServletResponse response, String userLoginType);

    void logout(CustomUserDetails userDetails, HttpServletResponse response);

    UserSummaryDto myInfo(CustomUserDetails user, String token);

    boolean check(CustomUserDetails user, String password);
}
