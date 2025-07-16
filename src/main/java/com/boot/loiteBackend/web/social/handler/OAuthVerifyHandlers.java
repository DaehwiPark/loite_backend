package com.boot.loiteBackend.web.social.handler;

import com.boot.loiteBackend.web.social.model.OAuthUserInfo;
import com.boot.loiteBackend.web.social.enums.ProviderType;

public interface OAuthVerifyHandlers {

    ProviderType getProvider();

    String getVerifyUrl();

    String requestVerifyAccessToken(String code);

    OAuthUserInfo getUserInfo(String accessToken);

}
