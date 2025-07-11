package com.boot.loiteBackend.web.social.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "소셜 계정 연동 정보 DTO")
public class SocialLinkingDto {

    @Schema(description = "소셜 로그인 제공자 타입", example = "KAKAO", allowableValues = {"GOOGLE", "KAKAO", "NAVER"})
    private String socialType;

    @Schema(description = "소셜 계정 이메일 주소", example = "socialuser@example.com")
    private String email;

    @Schema(description = "소셜 계정 이름", example = "홍길동")
    private String name;

    @Schema(description = "연동 시각", example = "2024-07-10T12:34:56")
    private LocalDateTime connectedAt;

    @Schema(description = "해당 소셜 계정으로 회원가입이 완료되었는지 여부", example = "true")
    private boolean isRegisterAccount;
}
