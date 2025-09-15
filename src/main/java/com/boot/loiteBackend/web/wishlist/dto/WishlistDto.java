package com.boot.loiteBackend.web.wishlist.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Schema(description = "위시리스트 응답 DTO")
public class WishlistDto {

    @Schema(description = "위시리스트 고유 ID", example = "10")
    private Long wishlistId;

    @Schema(description = "상품 ID", example = "101")
    private Long productId;

    @Schema(description = "상품명", example = "스마트 전기포트")
    private String productName;

    @Schema(description = "상품 대표 이미지 URL", example = "/uploads/product/image/kettle_main.png")
    private String productImageUrl;

    @Schema(description = "기존 금액(정가)", example = "79900.00")
    private BigDecimal originalPrice;

    @Schema(description = "할인율 (%)", example = "25")
    private Integer discountRate;

    @Schema(description = "할인 반영된 금액", example = "59900.00")
    private BigDecimal discountedPrice;

    @Schema(description = "찜한 시각", example = "2025-09-15T22:45:10")
    private LocalDateTime createdAt;
}
