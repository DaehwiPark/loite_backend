package com.boot.loiteBackend.web.social.handler;

import com.boot.loiteBackend.web.social.client.GoogleOAuthClient;
import com.boot.loiteBackend.web.social.dto.OAuthUserInfoDto;
import com.boot.loiteBackend.web.social.enums.ProviderType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoogleOAuthHandler implements OAuthHandler, OAuthLinkHandler, OAuthVerifyHandlers {

    private final GoogleOAuthClient googleOAuthClient;

    // 사용자 정보 요청 공통
    @Override
    public OAuthUserInfoDto getUserInfo(String accessToken) {
        return googleOAuthClient.requestUserInfo(accessToken);
    }

    @Override
    public ProviderType getProvider() {
        return ProviderType.GOOGLE;
    }

    // 로그인용
    @Override
    public String getUrl() {
        return googleOAuthClient.getLoginUrl();
    }

    @Override
    public String requestAccessToken(String code) {
        return googleOAuthClient.requestAccessToken(code);
    }

    // 연동용
    @Override
    public String getLinkUrl() {
        return googleOAuthClient.getLinkingUrl();
    }

    @Override
    public String requestLinkingAccessToken(String code) {
        return googleOAuthClient.requestLinkingAccessToken(code);
    }

    // 인증(verify)용
    @Override
    public String getVerifyUrl() {
        return googleOAuthClient.getVerifyUrl();
    }

    @Override
    public String requestVerifyAccessToken(String code) {
        return googleOAuthClient.requestVerifyAccessToken(code);
    }


}
