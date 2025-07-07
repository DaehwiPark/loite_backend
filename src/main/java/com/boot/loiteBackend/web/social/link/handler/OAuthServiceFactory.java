//package com.boot.loiteBackend.web.auth.oauth.link.handler;
//
//import com.boot.loiteBackend.web.auth.oauth.link.model.OAuthProvider;
//import com.boot.loiteBackend.web.auth.oauth.link.service.OAuthLoginService;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//
//@Component
//@RequiredArgsConstructor
//public class OAuthServiceFactory {
//
//    private final Map<OAuthProvider, OAuthLoginService> serviceMap;
//
//    public OAuthLoginService getService(String providerName) {
//        OAuthProvider provider = OAuthProvider.valueOf(providerName.toUpperCase());
//        return Optional.ofNullable(serviceMap.get(provider))
//                .orElseThrow(() -> new IllegalArgumentException("Unsupported provider: " + provider));
//    }
//
//    @PostConstruct
//    public void init(Map<String, OAuthLoginService> beans) {
//        Map<OAuthProvider, OAuthLoginService> map = new HashMap<>();
//        beans.forEach((k, v) -> {
//            if (v instanceof KakaoOAuthLoginService) map.put(OAuthProvider.KAKAO, v);
//            else if (v instanceof GoogleOAuthLoginService) map.put(OAuthProvider.GOOGLE, v);
//            else if (v instanceof NaverOAuthLoginService) map.put(OAuthProvider.NAVER, v);
//        });
//        this.serviceMap = map;
//    }
//}
