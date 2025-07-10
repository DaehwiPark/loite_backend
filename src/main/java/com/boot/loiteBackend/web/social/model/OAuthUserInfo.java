package com.boot.loiteBackend.web.social.dto;

public interface OAuthUserInfo {

    String getEmail();

    String getName();

    String getSocialId();

    String getProvider();
}
