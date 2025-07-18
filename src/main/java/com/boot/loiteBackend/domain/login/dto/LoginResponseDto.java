package com.boot.loiteBackend.domain.login.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "로그인 응답 DTO (JWT 토큰 정보 포함)")
public class LoginResponseDto {

    @Schema(description = "Access Token (JWT)", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

    @Schema(description = "Refresh Token (JWT, Redis에 저장됨)", example = "dGhpc0lzUmVmcmVzaFRva2VuMTIzNA==")
    private String refreshToken;

    @Builder.Default
    @Schema(description = "토큰 타입 (고정값)", example = "Bearer", defaultValue = "Bearer")
    private String tokenType = "Bearer";
}
