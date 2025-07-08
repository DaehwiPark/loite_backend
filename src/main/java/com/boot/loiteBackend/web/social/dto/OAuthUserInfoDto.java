package com.boot.loiteBackend.web.social.dto;

import com.boot.loiteBackend.web.social.enums.ProviderType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "OAuth 사용자 정보 DTO")
public class OAuthUserInfoDto {

    @Schema(description = "사용자 이메일", example = "user@example.com")
    private final String email;

    @Schema(description = "사용자 이름", example = "홍길동")
    private final String name;

    @Schema(description = "소셜 제공자 고유 식별자", example = "12345678901234567890")
    private final String socialId;

    @Schema(description = "소셜 로그인 제공자", example = "GOOGLE")
    private final ProviderType provider;
}
