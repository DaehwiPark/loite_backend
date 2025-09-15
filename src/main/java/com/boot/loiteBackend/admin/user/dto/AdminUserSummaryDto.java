package com.boot.loiteBackend.admin.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "사용자 정보 요약 DTO")
public class AdminUserSummaryDto {

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "사용자 이메일", example = "user@example.com")
    private String userEmail;

    @Schema(description = "사용자 이름", example = "홍길동")
    private String userName;

    @Schema(description = "가입 시 소셜 타입 또는 이메일", example = "KAKAO / NAVER / GOOGLE / EMAIL")
    private String userRegisterType;

    @Schema(description = "현재 로그인 방식", example = "KAKAO / NAVER / GOOGLE / EMAIL")
    private String userLoginType;

    @Schema(description = "사용자 역할", example = "ADMIN / USER")
    private String userRole;
}
