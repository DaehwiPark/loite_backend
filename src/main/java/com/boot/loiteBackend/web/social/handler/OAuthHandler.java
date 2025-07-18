package com.boot.loiteBackend.web.social.handler;

import com.boot.loiteBackend.web.social.model.OAuthUserInfo;
import com.boot.loiteBackend.web.social.enums.ProviderType;

public interface OAuthHandler {

    ProviderType getProvider();

    String getUrl();

    String requestAccessToken(String code);

    OAuthUserInfo getUserInfo(String accessToken);
}
