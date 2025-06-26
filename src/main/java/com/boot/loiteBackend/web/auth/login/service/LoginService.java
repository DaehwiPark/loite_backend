package com.boot.loiteBackend.web.auth.login.service;

import com.boot.loiteBackend.web.auth.login.dto.LoginRequestDto;
import com.boot.loiteBackend.web.auth.login.dto.LoginResponseDto;

public interface LoginService {
    LoginResponseDto login(LoginRequestDto dto);
}
