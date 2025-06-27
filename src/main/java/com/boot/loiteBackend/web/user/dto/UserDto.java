package com.boot.loiteBackend.web.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "사용자 정보 DTO")
public class UserDto {

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "사용자 이메일", example = "user@example.com")
    private String userEmail;

    @Schema(description = "사용자 이름", example = "홍길동")
    private String userName;

    @Schema(description = "휴대폰 번호", example = "010-1234-5678")
    private String userPhone;

    @Schema(description = "생년월일", example = "1990-01-01")
    private LocalDate userBirthdate;

    @Schema(description = "만 14세 이상 여부", example = "true")
    private Boolean isOver14;

    @Schema(description = "이용약관 동의 여부", example = "true")
    private Boolean agreeTerms;

    @Schema(description = "개인정보 수집 및 이용 동의 여부", example = "true")
    private Boolean agreePrivacy;

    @Schema(description = "광고/마케팅 수신 동의 여부", example = "true")
    private Boolean agreeMarketing;

    @Schema(description = "이메일 인증 여부", example = "true")
    private Boolean emailVerified;

    @Schema(description = "이메일 인증 일시", example = "2024-06-01T10:00:00")
    private LocalDateTime emailVerifiedAt;

    @Schema(description = "사용자 역할", example = "ADMIN / USER")
    private String role;

    @Schema(description = "회원 상태", example = "ACTIVE / WITHDRAWN")
    private String userStatus;

    @Schema(description = "마지막 로그인 일시", example = "2024-06-01T12:00:00")
    private LocalDateTime lastLoginAt;

    @Schema(description = "탈퇴 일시", example = "2024-06-05T11:30:00")
    private LocalDateTime withdrawnAt;

    @Schema(description = "계정 생성 일시", example = "2024-01-01T09:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "계정 수정 일시", example = "2024-06-03T15:20:00")
    private LocalDateTime updatedAt;
}
