package com.boot.loiteBackend.web.social.handler;

import com.boot.loiteBackend.web.social.client.GoogleOAuthClient;
import com.boot.loiteBackend.web.social.dto.google.GoogleUserResponseDto;
import com.boot.loiteBackend.web.social.dto.OAuthUserInfoDto;
import com.boot.loiteBackend.web.social.enums.ProviderType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoogleOAuthHandler implements OAuthHandler {

    private final GoogleOAuthClient googleOAuthClient;

    @Override
    public ProviderType getProvider() {
        return ProviderType.GOOGLE;
    }

    @Override
    public String getUrl() {
        return googleOAuthClient.buildLoginUrl();
    }

    @Override
    public String requestAccessToken(String code) {
        return googleOAuthClient.requestAccessToken(code);
    }

    @Override
    public OAuthUserInfoDto getUserInfo(String accessToken) {
        GoogleUserResponseDto user = googleOAuthClient.requestUserInfo(accessToken);

        return OAuthUserInfoDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .socialId(user.getSub())
                .provider(ProviderType.GOOGLE)
                .build();
    }
}
