package com.boot.loiteBackend.web.mileage.total.dto;

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
@Schema(description = "회원별 마일리지 잔액 및 통계 DTO")
public class MileageTotalDto {

    @Schema(description = "회원 ID", example = "1001")
    private Long userId;

    @Schema(description = "누적 적립 마일리지", example = "15000")
    private Integer mileageTotalEarned;

    @Schema(description = "누적 사용 마일리지", example = "5000")
    private Integer mileageTotalUsed;

    @Schema(description = "누적 소멸 마일리지", example = "2000")
    private Integer mileageTotalExpired;

    @Schema(description = "현재 보유 마일리지", example = "8000")
    private Integer mileageTotalAmount;

    @Schema(description = "요약 정보 생성일시", example = "2025-07-29T14:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "요약 정보 수정일시", example = "2025-07-29T15:00:00")
    private LocalDateTime updatedAt;
}