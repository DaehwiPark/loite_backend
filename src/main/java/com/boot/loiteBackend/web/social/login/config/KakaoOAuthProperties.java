package com.boot.loiteBackend.web.social.login.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "oauth.kakao")
public class KakaoOAuthProperties {
    private String authEndpoint;
    private String tokenEndpoint;
    private String clientId;
    private String redirectUri;
    private String responseType;
}