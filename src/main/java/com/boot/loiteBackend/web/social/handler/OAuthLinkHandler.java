package com.boot.loiteBackend.web.social.handler;

import com.boot.loiteBackend.web.social.dto.OAuthUserInfoDto;
import com.boot.loiteBackend.web.social.enums.ProviderType;

public interface OAuthLinkHandler {
    ProviderType getProvider();

    String getLinkUrl();

    String requestLinkingAccessToken(String code);

    OAuthUserInfoDto getUserInfo(String accessToken);

}
