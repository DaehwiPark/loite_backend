package com.boot.loiteBackend.web.user.general.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "사용자 상세 정보 DTO")
public class UserInfoDto {

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "이메일", example = "user@example.com")
    private String userEmail;

    @Schema(description = "이름", example = "홍길동")
    private String userName;

    @Schema(description = "휴대폰 번호", example = "010-1234-5678")
    private String userPhone;

    @Schema(description = "생년월일", example = "1990-01-01")
    private LocalDate userBirthdate;

    @Schema(description = "가입 방식", example = "EMAIL / KAKAO / NAVER / GOOGLE")
    private String userRegisterType;

    @Schema(description = "이용약관 동의", example = "true")
    private Boolean agreeTerms;

    @Schema(description = "개인정보 수집 동의", example = "true")
    private Boolean agreePrivacy;

    @Schema(description = "마케팅 수신 동의(SNS)", example = "true")
    private Boolean agreeMarketingSns;

    @Schema(description = "마케팅 수신 동의(이메일)", example = "true")
    private Boolean agreeMarketingEmail;

    @Schema(description = "만 14세 이상 여부", example = "true")
    private Boolean isOver14;

    @Schema(description = "마지막 로그인 시각", example = "2024-07-25T12:00:00")
    private LocalDateTime lastLoginAt;

    @Schema(description = "계정 생성 시각", example = "2024-01-01T09:00:00")
    private LocalDateTime createdAt;
}