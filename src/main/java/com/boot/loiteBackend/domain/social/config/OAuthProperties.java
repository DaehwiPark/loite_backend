package com.boot.loiteBackend.domain.social.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix = "oauth")
@Getter
@Setter
@Component
public class OAuthProperties {

    private Provider kakao;
    private Provider naver;
    private Provider google;

    @Getter
    @Setter
    public static class Provider {
        private String authEndpoint;
        private String tokenEndpoint;
        private String clientId;
        private String clientSecret;
        private String redirectUri;
        private String responseType;
        private String scope;  // 구글만 사용
    }
}
