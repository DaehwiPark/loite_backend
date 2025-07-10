package com.boot.loiteBackend.web.social.link.model;

public interface OAuthUserInfo {

    String getSocialId();

    String getEmail();

    String getName();

    String getProvider();
}
