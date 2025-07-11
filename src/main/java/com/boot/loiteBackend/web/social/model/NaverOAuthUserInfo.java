package com.boot.loiteBackend.web.social.model;

import com.boot.loiteBackend.web.social.dto.naver.NaverUserResponseDto;
import com.boot.loiteBackend.web.social.enums.ProviderType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NaverOAuthUserInfo implements OAuthUserInfo {

    private final NaverUserResponseDto naverUser;

    @Override
    public String getEmail() {
        return naverUser.getResponse() != null
                ? naverUser.getResponse().getEmail()
                : null;
    }

    @Override
    public String getName() {
        return naverUser.getResponse() != null
                ? naverUser.getResponse().getName()
                : null;
    }

    @Override
    public String getSocialId() {
        return naverUser.getResponse() != null
                ? naverUser.getResponse().getId()
                : null;
    }

    @Override
    public String getProvider() {
        return ProviderType.NAVER.name();
    }
}
