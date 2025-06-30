package com.boot.loiteBackend.web.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "사용자 정보 요약 DTO")
public class UserSummaryDto {

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "사용자 이메일", example = "user@example.com")
    private String userEmail;

    @Schema(description = "사용자 이름", example = "홍길동")
    private String userName;

    @Schema(description = "사용자 역할", example = "ADMIN / USER")
    private String role;

}
