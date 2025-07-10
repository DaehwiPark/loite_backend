package com.boot.loiteBackend.web.social.handler;

import com.boot.loiteBackend.web.social.client.NaverOAuthClient;
import com.boot.loiteBackend.web.social.dto.OAuthUserInfoDto;
import com.boot.loiteBackend.web.social.enums.ProviderType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NaverOAuthHandler implements OAuthHandler, OAuthLinkHandler {

    private final NaverOAuthClient naverOAuthClient;

    @Override
    public ProviderType getProvider() {
        return ProviderType.NAVER;
    }

    @Override
    public String getUrl() {
        return naverOAuthClient.getLoginUrl();
    }

    @Override
    public String getLinkUrl() {
        return naverOAuthClient.getLinkingUrl();
    }

    @Override
    public String requestAccessToken(String code) {
        return naverOAuthClient.requestAccessToken(code);
    }

    @Override
    public String requestLinkingAccessToken(String code) {
        return naverOAuthClient.requestAccessToken(code);
    }

    @Override
    public OAuthUserInfoDto getUserInfo(String accessToken) {
        return naverOAuthClient.requestUserInfo(accessToken);
    }
}
