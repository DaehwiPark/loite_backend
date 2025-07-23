package com.boot.loiteBackend.web.product.dto;

import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import com.boot.loiteBackend.admin.product.product.entity.AdminProductImageEntity;
import com.boot.loiteBackend.admin.product.product.enums.ImageType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ProductMainResponseDto {

    @Schema(description = "상품 ID", example = "1001")
    private Long productId;

    @Schema(description = "상품명", example = "프리미엄 원목 테이블")
    private String productName;

    @Schema(description = "정가", example = "299000.00")
    private BigDecimal productPrice;

    @Schema(description = "할인가", example = "269100.00")
    private BigDecimal discountedPrice;

    @Schema(description = "할인율 (%)", example = "10")
    private Integer discountRate;

    @Schema(description = "대표 이미지 URL", example = "https://cdn.loite.com/images/product/wood-table-main.jpg")
    private String imageUrl;
}

