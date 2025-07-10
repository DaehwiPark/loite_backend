package com.boot.loiteBackend.web.social.handler;

import com.boot.loiteBackend.web.social.dto.OAuthUserInfoDto;
import com.boot.loiteBackend.web.social.enums.ProviderType;

public interface OAuthVerifyHandlers {

    ProviderType getProvider();

    String getVerifyUrl();

    String requestVerifyAccessToken(String code);

    OAuthUserInfoDto getUserInfo(String accessToken);
}
