package com.boot.loiteBackend.web.social.dto.naver;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Naver OAuth 토큰 응답 DTO (네이버 인증 서버에서 받은 토큰 정보)")
public class NaverTokenResponseDto {

    @JsonProperty("access_token")
    @Schema(description = "Access Token (API 요청 시 사용되는 인증 토큰)", example = "AAAABBBBCCCCDDDD")
    private String accessToken;

    @JsonProperty("token_type")
    @Schema(description = "토큰 타입 (일반적으로 'bearer')", example = "bearer")
    private String tokenType;

    @JsonProperty("refresh_token")
    @Schema(description = "Refresh Token (Access Token 재발급용)", example = "ZZZZYYYYXXXXWWWW")
    private String refreshToken;

    @JsonProperty("expires_in")
    @Schema(description = "Access Token 만료 시간 (초 단위)", example = "3600")
    private Integer expiresIn;

    @Schema(description = "Access Token 권한 범위", example = "name email")
    private String scope;

    @JsonProperty("refresh_token_expires_in")
    @Schema(description = "Refresh Token 만료 시간 (초 단위)", example = "5184000")
    private Integer refreshTokenExpiresIn;
}
