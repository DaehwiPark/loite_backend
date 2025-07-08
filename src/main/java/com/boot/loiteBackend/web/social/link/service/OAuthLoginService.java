package com.boot.loiteBackend.web.social.link.service;

import com.boot.loiteBackend.domain.login.dto.LoginResponseDto;
import jakarta.servlet.http.HttpServletResponse;

public interface OAuthLoginService {
    String getLoginRedirectUrl();
    LoginResponseDto loginWithOAuth(String code, HttpServletResponse response);
}
