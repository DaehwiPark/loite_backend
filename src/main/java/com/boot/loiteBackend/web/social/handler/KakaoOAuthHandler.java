package com.boot.loiteBackend.web.social.handler;

import com.boot.loiteBackend.web.social.client.KakaoOAuthClient;
import com.boot.loiteBackend.web.social.dto.OAuthUserInfoDto;
import com.boot.loiteBackend.web.social.dto.kakao.KakaoUserResponseDto;
import com.boot.loiteBackend.web.social.enums.ProviderType;
import com.boot.loiteBackend.web.social.error.SocialErrorCode;
import com.boot.loiteBackend.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KakaoOAuthHandler implements OAuthHandler, OAuthLinkHandler {

    private final KakaoOAuthClient kakaoOAuthClient;

    @Override
    public ProviderType getProvider() {
        return ProviderType.KAKAO;
    }

    // 로그인 URL
    @Override
    public String getUrl() {
        return kakaoOAuthClient.getLoginUrl();
    }

    // 연동용 URL 추가
    @Override
    public String getLinkUrl() {
        return kakaoOAuthClient.getLinkingUrl();
    }

    @Override
    public String requestAccessToken(String code) {
        return kakaoOAuthClient.requestAccessToken(code);
    }

    @Override
    public OAuthUserInfoDto getUserInfo(String accessToken) {
        KakaoUserResponseDto user = kakaoOAuthClient.requestUserInfo(accessToken);

        if (user != null && user.getKakao_account() != null) {
            return OAuthUserInfoDto.builder()
                    .email(user.getKakao_account().getEmail())
                    .name(user.getKakao_account().getProfile().getNickname())
                    .socialId(user.getId().toString())
                    .provider(ProviderType.KAKAO)
                    .build();
        }

        throw new CustomException(SocialErrorCode.FAILED_TO_GET_USER);
    }
}
