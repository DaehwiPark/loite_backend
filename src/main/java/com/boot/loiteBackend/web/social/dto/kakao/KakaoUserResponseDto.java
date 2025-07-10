package com.boot.loiteBackend.web.social.dto.kakao;

import com.boot.loiteBackend.web.social.model.OAuthUserInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "카카오 사용자 정보 응답 DTO")
public class KakaoUserResponseDto implements OAuthUserInfo {

    @Schema(description = "카카오 사용자 고유 ID", example = "123456789")
    private Long id;

    @Schema(description = "카카오 계정 정보")
    private KakaoAccount kakao_account;

    @Override
    public String getSocialId() {
        return id != null ? String.valueOf(id) : null;
    }

    @Override
    public String getEmail() {
        return kakao_account != null ? kakao_account.getEmail() : null;
    }

    @Override
    public String getName() {
        if (kakao_account != null && kakao_account.getProfile() != null) {
            return kakao_account.getProfile().getNickname();
        }
        return null;
    }

    @Override
    public String getProvider() {
        return "KAKAO";
    }

    @Getter
    @Setter
    @Schema(description = "카카오 계정 상세 정보")
    public static class KakaoAccount {

        @Schema(description = "카카오 계정 이메일", example = "user@kakao.com")
        private String email;

        @Schema(description = "프로필 정보")
        private Profile profile;

        @Getter
        @Setter
        @Schema(description = "프로필 정보 세부")
        public static class Profile {

            @Schema(description = "닉네임", example = "홍길동")
            private String nickname;
        }
    }
}
