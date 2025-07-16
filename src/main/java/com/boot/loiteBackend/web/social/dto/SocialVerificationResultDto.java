package com.boot.loiteBackend.web.social.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "소셜 인증 결과 응답 DTO")
public class SocialVerificationResultDto {

    @Schema(description = "인증 일치 여부", example = "true")
    private boolean verified;

    @Schema(description = "발급된 소셜 access token", example = "ya29.a0AfH6SMC... (생략)")
    private String accessToken;
}
