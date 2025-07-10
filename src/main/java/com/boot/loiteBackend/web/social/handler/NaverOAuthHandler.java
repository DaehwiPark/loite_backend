package com.boot.loiteBackend.web.social.handler;

import com.boot.loiteBackend.web.social.client.NaverOAuthClient;
import com.boot.loiteBackend.web.social.model.OAuthUserInfo;
import com.boot.loiteBackend.web.social.enums.ProviderType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NaverOAuthHandler implements OAuthHandler, OAuthLinkHandler, OAuthVerifyHandlers {

    private final NaverOAuthClient naverOAuthClient;

    // 사용자 정보 요청 공통
    @Override
    public OAuthUserInfo getUserInfo(String accessToken) {
        return naverOAuthClient.requestUserInfo(accessToken);
    }

    @Override
    public ProviderType getProvider() {
        return ProviderType.NAVER;
    }

    // 로그인용
    @Override
    public String getUrl() {
        return naverOAuthClient.getLoginUrl();
    }

    @Override
    public String requestAccessToken(String code) {
        return naverOAuthClient.requestAccessToken(code);
    }

    // 연동용
    @Override
    public String getLinkUrl() {
        return naverOAuthClient.getLinkingUrl();
    }

    @Override
    public String requestLinkingAccessToken(String code) {
        return naverOAuthClient.requestLinkingAccessToken(code);
    }

    // 인증(verify)용
    @Override
    public String getVerifyUrl() {
        return naverOAuthClient.getVerifyUrl();
    }

    @Override
    public String requestVerifyAccessToken(String code) {
        return naverOAuthClient.requestVerifyAccessToken(code);
    }
}
