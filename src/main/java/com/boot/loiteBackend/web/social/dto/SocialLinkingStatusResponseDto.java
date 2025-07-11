package com.boot.loiteBackend.web.social.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "소셜 연동 상태 응답 DTO (회원가입 방식 및 연동된 소셜 계정 정보 포함)")
public class SocialLinkingStatusResponseDto {

    @Schema(
            description = "회원가입 방식 (예: EMAIL, GOOGLE, KAKAO, NAVER)",
            example = "EMAIL",
            allowableValues = {"EMAIL", "GOOGLE", "KAKAO", "NAVER"}
    )
    private String userRegisterType;

    @Schema(
            description = "연동된 소셜 계정 목록",
            example = "[{ \"socialType\": \"KAKAO\", \"email\": \"test@kakao.com\", \"name\": \"홍길동\", \"connectedAt\": \"2024-07-10T10:20:30\", \"isRegisterAccount\": true }]"
    )
    private List<SocialLinkingDto> socialLinking;
}
