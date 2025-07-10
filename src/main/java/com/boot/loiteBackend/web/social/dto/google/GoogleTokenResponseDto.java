package com.boot.loiteBackend.web.social.dto.google;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Google OAuth 토큰 응답 DTO (구글 인증 서버에서 반환된 토큰 정보)")
public class GoogleTokenResponseDto {

    @Schema(description = "Access Token", example = "ya29.a0AfH6SMD...")
    @JsonProperty("access_token")
    private String accessToken;

    @Schema(description = "토큰 타입 (예: Bearer)", example = "Bearer")
    @JsonProperty("token_type")
    private String tokenType;

    @Schema(description = "Refresh Token", example = "1//0gSDFsdfsdf9-rg")
    @JsonProperty("refresh_token")
    private String refreshToken;

    @Schema(description = "Access Token 만료 시간(초)", example = "3599")
    @JsonProperty("expires_in")
    private Integer expiresIn;

    @Schema(description = "요청한 권한 범위", example = "openid email profile")
    private String scope;

    @Schema(description = "Refresh Token 만료 시간(초)", example = "5184000")
    @JsonProperty("refresh_token_expires_in")
    private Integer refreshTokenExpiresIn;
}
