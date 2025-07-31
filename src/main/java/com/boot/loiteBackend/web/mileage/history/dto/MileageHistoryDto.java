package com.boot.loiteBackend.web.mileage.history.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "마일리지 적립/사용/소멸 내역 DTO")
public class MileageHistoryDto {

    @Schema(
            description = "회원 ID (마일리지 소유자)",
            example = "1001",
            requiredMode = RequiredMode.REQUIRED
    )
    private Long userId;

    @Schema(
            description = "마일리지 타입 (EARN=적립, USE=사용, EXPIRE=소멸)",
            example = "EARN",
            requiredMode = RequiredMode.REQUIRED
    )
    private String mileageHistoryType;

    @Schema(
            description = "적립/사용 사유 (예: 주문번호, 회원가입 등)",
            example = "SIGN_UP",
            requiredMode = RequiredMode.NOT_REQUIRED
    )
    private String mileageHistorySource;

    @Schema(
            description = "마일리지 변동량 (+적립, -사용)",
            example = "1000",
            requiredMode = RequiredMode.REQUIRED
    )
    private Integer mileageHistoryAmount;

    @Schema(
            description = "해당 시점의 마일리지 잔액",
            example = "2000",
            requiredMode = RequiredMode.NOT_REQUIRED
    )
    private Integer mileageHistoryTotalAmount;

    @Schema(
            description = "마일리지 정책 ID (적립인 경우에만 사용)",
            example = "3",
            requiredMode = RequiredMode.NOT_REQUIRED
    )
    private Long mileagePolicyId;

    @Schema(
            description = "주문 ID (주문과 연관된 경우)",
            example = "202507290001",
            requiredMode = RequiredMode.NOT_REQUIRED
    )
    private Long orderId;

    @Schema(
            description = "마일리지 적립 일시",
            example = "2025-07-29T15:30:00",
            requiredMode = RequiredMode.REQUIRED
    )
    private LocalDateTime createdAt;
}