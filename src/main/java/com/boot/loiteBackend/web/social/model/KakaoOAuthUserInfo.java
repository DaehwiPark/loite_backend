package com.boot.loiteBackend.web.social.model;

import com.boot.loiteBackend.web.social.dto.kakao.KakaoUserResponseDto;
import com.boot.loiteBackend.web.social.enums.ProviderType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KakaoOAuthUserInfo implements OAuthUserInfo {

    private final KakaoUserResponseDto kakaoUser;

    @Override
    public String getEmail() {
        return kakaoUser.getKakao_account() != null
                ? kakaoUser.getKakao_account().getEmail()
                : null;
    }

    @Override
    public String getName() {
        return kakaoUser.getKakao_account() != null &&
                kakaoUser.getKakao_account().getProfile() != null
                ? kakaoUser.getKakao_account().getProfile().getNickname()
                : null;
    }

    @Override
    public String getSocialId() {
        return kakaoUser.getId() != null
                ? kakaoUser.getId().toString()
                : null;
    }

    @Override
    public String getProvider() {
        return ProviderType.KAKAO.name();
    }
}
