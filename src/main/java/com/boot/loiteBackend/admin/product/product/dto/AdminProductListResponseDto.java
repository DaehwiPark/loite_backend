package com.boot.loiteBackend.admin.product.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AdminProductListResponseDto {

    @Schema(description = "상품 등록일시", example = "2025-07-20T14:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "상품 활성화 여부 (Y/N)", example = "Y")
    private String activeYn;

    @Schema(description = "상품명", example = "프리미엄 원목 테이블")
    private String productName;

    @Schema(description = "브랜드명", example = "로이테가구")
    private String brandName;

    @Schema(description = "판매가", example = "199000.00")
    private BigDecimal productPrice;

    @Schema(description = "할인율 (%)", example = "10")
    private Integer discountRate;

    @Schema(description = "할인가", example = "179100.00")
    private BigDecimal discountedPrice;

    @Schema(description = "조회수", example = "253")
    private int viewCount;

    @Schema(description = "누적 판매 수량", example = "87")
    private int salesCount;
}

