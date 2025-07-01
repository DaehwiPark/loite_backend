package com.boot.loiteBackend.web.auth.oauth.link.service;

import com.boot.loiteBackend.web.auth.login.dto.LoginResponseDto;
import jakarta.servlet.http.HttpServletResponse;

public interface OAuthLoginService {
    String getLoginRedirectUrl();
    LoginResponseDto loginWithOAuth(String code, HttpServletResponse response);
}
