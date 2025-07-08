package com.boot.loiteBackend.web.social.config;

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
        private String authEndpoint;         // 인증 요청 URL
        private String tokenEndpoint;        // 토큰 요청 URL
        private String clientId;
        private String clientSecret;
        private String redirectUri;          // 로그인용 redirect_uri
        private String linkRedirectUri;      // 소셜 연동용 redirect_uri (신규 추가)
        private String responseType;
        private String scope;                // 구글 전용
    }
}
