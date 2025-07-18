package com.boot.loiteBackend.web.social.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Kakao OAuth 토큰 응답 DTO (카카오 인증 서버에서 받은 토큰 정보)")
public class KakaoTokenResponseDto {

    @Schema(description = "Access Token", example = "v7LuUq4Vb9s...")
    @JsonProperty("access_token")
    private String accessToken;

    @Schema(description = "토큰 타입 (예: bearer)", example = "bearer")
    @JsonProperty("token_type")
    private String tokenType;

    @Schema(description = "Refresh Token", example = "jLk3hSDnYXg...")
    @JsonProperty("refresh_token")
    private String refreshToken;

    @Schema(description = "Access Token의 만료 시간 (초 단위)", example = "21599")
    @JsonProperty("expires_in")
    private Integer expiresIn;

    @Schema(description = "Access Token의 권한 범위", example = "account_email profile_image")
    private String scope;

    @Schema(description = "Refresh Token의 만료 시간 (초 단위)", example = "5183999")
    @JsonProperty("refresh_token_expires_in")
    private Integer refreshTokenExpiresIn;
}
