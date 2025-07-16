package com.boot.loiteBackend.web.social.handler;

import com.boot.loiteBackend.web.social.enums.ProviderType;

public interface OAuthUnLinkHandlers {

    ProviderType getProvider();

    void unlinkSocialAccount(String socialId, String email);

}
