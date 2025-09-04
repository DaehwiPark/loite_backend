package com.boot.loiteBackend.admin.product.additional.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminAdditionalResponseDto {

    @Schema(description = "추가구성품 ID", example = "1")
    private Long additionalId;

    @Schema(description = "본상품 ID", example = "101")
    private Long productId;

    @Schema(description = "추가구성품 이름", example = "전용 거치대")
    private String additionalName;

    @Schema(description = "추가구성품 재고 수량", example = "100")
    private Integer additionalStock;

    @Schema(description = "추가구성품 가격", example = "15000")
    private BigDecimal additionalPrice;

    @Schema(description = "추가구성품 이미지", example = "example")
    private String additionalImageUrl;

    @Schema(description = "사용 여부 (Y/N)", example = "Y")
    private Boolean activeYn;

    @Schema(description = "생성 일시", example = "2025-09-01T12:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정 일시", example = "2025-09-01T12:10:00")
    private LocalDateTime updatedAt;
}

