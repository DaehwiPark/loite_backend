package com.boot.loiteBackend.web.social.handler;

import com.boot.loiteBackend.web.social.dto.OAuthUserInfoDto;
import com.boot.loiteBackend.web.social.enums.ProviderType;

public interface OAuthHandler {

    ProviderType getProvider();

    String getUrl();

    String requestAccessToken(String code);

    OAuthUserInfoDto getUserInfo(String accessToken);
}
