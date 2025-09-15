package com.boot.loiteBackend.admin.login.service;

import com.boot.loiteBackend.admin.login.dto.AdminLoginRequestDto;
import com.boot.loiteBackend.admin.user.dto.AdminUserSummaryDto;
import com.boot.loiteBackend.config.security.CustomUserDetails;
import com.boot.loiteBackend.web.login.dto.LoginResponseDto;
import com.boot.loiteBackend.web.user.general.dto.UserSummaryDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AdminLoginService {
    LoginResponseDto login(AdminLoginRequestDto req, HttpServletResponse response);
    void logout(CustomUserDetails userDetails, HttpServletResponse response);
    AdminUserSummaryDto myInfo(CustomUserDetails user, String token);
    boolean check(CustomUserDetails user, String password);
}
