package com.boot.loiteBackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppEnvironmentConfig {

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    public boolean isDev() {
        return "dev".equalsIgnoreCase(activeProfile);
    }

    public boolean isSecureCookieEnabled() {
        return !isDev(); // dev 제외한 환경은 secure 쿠키,  HTTPS 요청에서만 브라우저가 전송되도록
    }
}
