package com.boot.loiteBackend.web.social.handler;

import com.boot.loiteBackend.web.social.dto.OAuthUserInfoDto;
import com.boot.loiteBackend.web.social.enums.ProviderType;

public interface OAuthHandler {
    ProviderType getProvider();                        // enum: KAKAO, NAVER, GOOGLE
    String getUrl();                              // 로그인 URL 생성
    String requestAccessToken(String code);            // access_token 발급
    OAuthUserInfoDto getUserInfo(String accessToken);     // 사용자 정보 표준화
}
