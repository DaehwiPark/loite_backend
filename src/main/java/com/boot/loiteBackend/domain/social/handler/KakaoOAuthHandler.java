package com.boot.loiteBackend.domain.social.handler;

import com.boot.loiteBackend.domain.social.client.KakaoOAuthClient;
import com.boot.loiteBackend.domain.social.dto.OAuthUserInfoDto;
import com.boot.loiteBackend.domain.social.dto.kakao.KakaoUserResponseDto;
import com.boot.loiteBackend.domain.social.enums.ProviderType;
import com.boot.loiteBackend.domain.social.error.SocialLoginErrorCode;
import com.boot.loiteBackend.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KakaoOAuthHandler implements OAuthHandler {

    private final KakaoOAuthClient kakaoOAuthClient;

    @Override
    public ProviderType getProvider() {
        return ProviderType.KAKAO;
    }

    @Override
    public String getLoginUrl() {
        return kakaoOAuthClient.getLoginUrl();
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

        throw new CustomException(SocialLoginErrorCode.FAILED_TO_GET_USER);
    }
}
