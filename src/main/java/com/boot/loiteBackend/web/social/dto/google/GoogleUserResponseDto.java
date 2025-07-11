package com.boot.loiteBackend.web.social.dto.google;

import com.boot.loiteBackend.web.social.model.OAuthUserInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Google 사용자 정보 응답 DTO")
public class GoogleUserResponseDto implements OAuthUserInfo {

    @Schema(description = "Google 고유 사용자 ID", example = "113475829103984798132")
    private String sub;

    @Schema(description = "사용자 이메일 주소", example = "example@gmail.com")
    private String email;

    @Schema(description = "이메일 인증 여부", example = "true")
    private boolean email_verified;

    @Schema(description = "전체 이름", example = "홍길동")
    private String name;

    @Schema(description = "이름(성 제외)", example = "길동")
    private String given_name;

    @Schema(description = "성", example = "홍")
    private String family_name;

    @Schema(description = "프로필 이미지 URL", example = "https://lh3.googleusercontent.com/a/...")
    private String picture;

    @Schema(description = "사용자 지역 설정", example = "ko")
    private String locale;

    @Override
    public String getSocialId() {
        return sub;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getProvider() {
        return "GOOGLE";
    }
}
