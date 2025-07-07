package com.boot.loiteBackend.web.social.link.model;

public interface OAuthUserInfo {
    String getEmail();
    String getName();
    String getId();
    String getProvider();
}
