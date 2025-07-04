package com.boot.loiteBackend.domain.social.resolver;

import com.boot.loiteBackend.domain.social.error.SocialLoginErrorCode;
import com.boot.loiteBackend.domain.social.handler.OAuthHandler;
import com.boot.loiteBackend.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OAuthHandlerResolver {

    private final List<OAuthHandler> handlers;

    public OAuthHandler resolve(String providerName) {
        return handlers.stream()
                .filter(handler -> handler.getProvider().name().equalsIgnoreCase(providerName))
                .findFirst()
                .orElseThrow(() -> new CustomException(SocialLoginErrorCode.UNSUPPORTED_PROVIDER));
    }
}
