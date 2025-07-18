package com.boot.loiteBackend.web.social.model;

public interface OAuthUserInfo {

    String getEmail();

    String getName();

    String getSocialId();

    String getProvider();
}
