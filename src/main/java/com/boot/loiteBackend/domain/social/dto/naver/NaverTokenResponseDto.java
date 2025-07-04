package com.boot.loiteBackend.domain.social.dto.naver;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "네이버 토큰 응답 DTO")
public class NaverTokenResponseDto {

    @JsonProperty("access_token")
    @Schema(description = "액세스 토큰", example = "AAAABBBBCCCCDDDD")
    private String accessToken;

    @JsonProperty("token_type")
    @Schema(description = "토큰 타입", example = "bearer")
    private String tokenType;

    @JsonProperty("refresh_token")
    @Schema(description = "리프레시 토큰", example = "ZZZZYYYYXXXXWWWW")
    private String refreshToken;

    @JsonProperty("expires_in")
    @Schema(description = "액세스 토큰 만료 시간(초)", example = "3600")
    private Integer expiresIn;

    @Schema(description = "토큰 권한 범위", example = "name email")
    private String scope;

    @JsonProperty("refresh_token_expires_in")
    @Schema(description = "리프레시 토큰 만료 시간(초)", example = "5184000")
    private Integer refreshTokenExpiresIn;
}
