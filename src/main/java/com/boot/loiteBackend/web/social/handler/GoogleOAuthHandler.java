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
public class GoogleOAuthHandler implements OAuthHandler, OAuthLinkHandler {

    private final GoogleOAuthClient googleOAuthClient;

    @Override
    public ProviderType getProvider() {
        return ProviderType.GOOGLE;
    }

    @Override
    public String getUrl() {
        return googleOAuthClient.getLoginUrl();
    }

    @Override
    public String getLinkUrl() {
        return googleOAuthClient.getLinkingUrl();
    }

    @Override
    public String requestAccessToken(String code) {
        return googleOAuthClient.requestAccessToken(code);
    }

    @Override
    public String requestLinkingAccessToken(String code) {
        return googleOAuthClient.requestLinkingAccessToken(code);
    }

    @Override
    public OAuthUserInfoDto getUserInfo(String accessToken) {
        return googleOAuthClient.requestUserInfo(accessToken);
    }
}
