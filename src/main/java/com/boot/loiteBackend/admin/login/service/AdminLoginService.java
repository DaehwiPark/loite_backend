package com.boot.loiteBackend.admin.login.service;

import com.boot.loiteBackend.admin.login.dto.AdminLoginRequestDto;
import com.boot.loiteBackend.web.login.dto.LoginResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AdminLoginService {
    LoginResponseDto login(AdminLoginRequestDto req, HttpServletResponse response);
    void logout(HttpServletRequest request, HttpServletResponse response);
}
