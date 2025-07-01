package com.boot.loiteBackend.web.auth.oauth.link.model;

public interface OAuthUserInfo {
    String getEmail();
    String getName();
    String getId();
    String getProvider();
}
