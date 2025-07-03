package com.boot.loiteBackend.web.social.login.service;


import com.boot.loiteBackend.global.response.ApiResponse;
import com.boot.loiteBackend.web.login.dto.LoginResponseDto;
import com.boot.loiteBackend.web.social.login.dto.SocialUserRegistrationDto;
import jakarta.servlet.http.HttpServletResponse;

public interface KakaoLoginService {
    String getKakaoLoginUrl();

    ApiResponse<LoginResponseDto> kakaoLoginCallback(String code, HttpServletResponse response);

    LoginResponseDto registerKakaoUser(SocialUserRegistrationDto registrationDto, HttpServletResponse response);

    Object getKakaoUserInfo(String accessToken);

}
