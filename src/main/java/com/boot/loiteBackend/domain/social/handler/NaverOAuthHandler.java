package com.boot.loiteBackend.domain.social.handler;

import com.boot.loiteBackend.domain.social.client.NaverOAuthClient;
import com.boot.loiteBackend.domain.social.dto.OAuthUserInfoDto;
import com.boot.loiteBackend.domain.social.dto.naver.NaverUserResponseDto;
import com.boot.loiteBackend.domain.social.enums.ProviderType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NaverOAuthHandler implements OAuthHandler {

    private final NaverOAuthClient naverOAuthClient;

    @Override
    public ProviderType getProvider() {
        return ProviderType.NAVER;
    }

    @Override
    public String getLoginUrl() {
        return naverOAuthClient.getLoginUrl();
    }

    @Override
    public String requestAccessToken(String code) {
        return naverOAuthClient.requestAccessToken(code);
    }

    @Override
    public OAuthUserInfoDto getUserInfo(String accessToken) {
        NaverUserResponseDto response = naverOAuthClient.requestUserInfo(accessToken);

        return OAuthUserInfoDto.builder()
                .socialId(response.getResponse().getId())
                .email(response.getResponse().getEmail())
                .name(response.getResponse().getName())
                .provider(ProviderType.NAVER)
                .build();
    }
}
