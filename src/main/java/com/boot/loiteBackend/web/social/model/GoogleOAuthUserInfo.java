package com.boot.loiteBackend.web.social.model;

import com.boot.loiteBackend.web.social.dto.google.GoogleUserResponseDto;
import com.boot.loiteBackend.web.social.enums.ProviderType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GoogleOAuthUserInfo implements OAuthUserInfo {

    private final GoogleUserResponseDto googleUser;

    @Override
    public String getEmail() {
        return googleUser.getEmail();
    }

    @Override
    public String getName() {
        return googleUser.getName();
    }

    @Override
    public String getSocialId() {
        return googleUser.getSub();
    }

    @Override
    public String getProvider() {
        return ProviderType.GOOGLE.name();
    }
}
