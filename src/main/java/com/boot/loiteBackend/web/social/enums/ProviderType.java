package com.boot.loiteBackend.web.social.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "소셜 로그인 공급자 타입")
public enum ProviderType {

    @Schema(description = "카카오 로그인")
    KAKAO,

    @Schema(description = "네이버 로그인")
    NAVER,

    @Schema(description = "구글 로그인")
    GOOGLE
}
