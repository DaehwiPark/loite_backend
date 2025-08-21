//package com.boot.loiteBackend.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//@Component
//public class AppEnvironmentConfig {
//
//    @Value("${spring.profiles.active:dev}")
//    private String activeProfile;
//
//    public boolean isDev() {
//        return "dev".equalsIgnoreCase(activeProfile);
//    }
//
//    public boolean isProd() {
//        return "prod".equalsIgnoreCase(activeProfile);
//    }
//    // dev 제외한 환경은 secure 쿠키,  HTTPS 요청에서만 브라우저가 전송되도록
//    public boolean isSecureCookieEnabled() {
//        // 개발서버에 ssl 인증 후에는 prod를 제거 필요
//        return !isDev() && !isProd();
//    }
//}
