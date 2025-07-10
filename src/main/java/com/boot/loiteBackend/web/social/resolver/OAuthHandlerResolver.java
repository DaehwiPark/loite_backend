package com.boot.loiteBackend.web.social.resolver;

import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.web.social.error.SocialErrorCode;
import com.boot.loiteBackend.web.social.handler.OAuthHandler;
import com.boot.loiteBackend.web.social.handler.OAuthLinkHandler;
import com.boot.loiteBackend.web.social.handler.OAuthVerifyHandlers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OAuthHandlerResolver {

    private final List<OAuthHandler> loginHandlers;
    private final List<OAuthLinkHandler> linkHandlers;
    private final List<OAuthVerifyHandlers> verifyHandlers;

    // 로그인용 핸들러
    public OAuthHandler resolveLogin(String providerName) {
        return loginHandlers.stream()
                .filter(handler -> handler.getProvider().name().equalsIgnoreCase(providerName))
                .findFirst()
                .orElseThrow(() -> new CustomException(SocialErrorCode.UNSUPPORTED_PROVIDER));
    }

    // 연동용 핸들러
    public OAuthLinkHandler resolveLink(String providerName) {
        return linkHandlers.stream()
                .filter(handler -> handler.getProvider().name().equalsIgnoreCase(providerName))
                .findFirst()
                .orElseThrow(() -> new CustomException(SocialErrorCode.UNSUPPORTED_PROVIDER));
    }

    // 인증용 핸들러
    public OAuthVerifyHandlers resolveVerify(String providerName) {
        return verifyHandlers.stream()
                .filter(handler -> handler.getProvider().name().equalsIgnoreCase(providerName))
                .findFirst()
                .orElseThrow(() -> new CustomException(SocialErrorCode.UNSUPPORTED_PROVIDER));
    }
}
