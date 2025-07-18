package com.boot.loiteBackend.domain.token.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Access Token 재발급 요청 DTO (Refresh Token 필요)")
public class TokenRequestDto {

    @Schema(
            description = "Refresh Token (Redis에 저장된 JWT 토큰)",
            example = "dGhpc0lzUmVmcmVzaFRva2VuMTIzNA==",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String refreshToken;
}
