package com.boot.loiteBackend.web.social.handler;

import com.boot.loiteBackend.web.social.client.KakaoOAuthClient;
import com.boot.loiteBackend.web.social.model.OAuthUserInfo;
import com.boot.loiteBackend.web.social.enums.ProviderType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoOAuthHandler implements OAuthHandler, OAuthLinkHandler, OAuthVerifyHandlers {

    private final KakaoOAuthClient kakaoOAuthClient;

    // 사용자 정보 요청 공통
    @Override
    public ProviderType getProvider() {
        return ProviderType.KAKAO;
    }

    @Override
    public OAuthUserInfo getUserInfo(String accessToken) {
        return kakaoOAuthClient.requestUserInfo(accessToken);
    }

    // 로그인용
    @Override
    public String getUrl() {
        return kakaoOAuthClient.getLoginUrl();
    }

    @Override
    public String requestAccessToken(String code) {
        return kakaoOAuthClient.requestAccessToken(code);
    }

    // 연동용
    @Override
    public String getLinkUrl() {
        return kakaoOAuthClient.getLinkingUrl();
    }

    @Override
    public String requestLinkingAccessToken(String code) {
        return kakaoOAuthClient.requestLinkingAccessToken(code);
    }

    // 인증(verify)용
    @Override
    public String getVerifyUrl() {
        return kakaoOAuthClient.getVerifyUrl();
    }

    @Override
    public String requestVerifyAccessToken(String code) {
        return kakaoOAuthClient.requestVerifyAccessToken(code);
    }
}
