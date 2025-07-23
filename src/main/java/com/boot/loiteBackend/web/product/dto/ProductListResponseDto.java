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
public class ProductListResponseDto {

    @Schema(description = "상품 ID", example = "1001")
    private Long productId;

    @Schema(description = "상품명", example = "프리미엄 원목 테이블")
    private String productName;

    @Schema(description = "브랜드명", example = "로이테가구")
    private String brandName;

    @Schema(description = "정가", example = "299000.00")
    private BigDecimal productPrice;

    @Schema(description = "할인가", example = "269100.00")
    private BigDecimal discountedPrice;

    @Schema(description = "할인율 (%)", example = "10")
    private Integer discountRate;

    @Schema(description = "대표 이미지 URL", example = "https://cdn.loite.com/images/product/wood-table-main.jpg")
    private String imageUrl;

    @Schema(description = "총 재고 수량", example = "20")
    private Integer stock;

    @Schema(description = "품절 여부", example = "false")
    private boolean soldOutYn;

    // private Double reviewRating;  // 추후 확장 예정

    public static ProductListResponseDto from(AdminProductEntity entity) {
        String imageUrl = entity.getProductImages().stream()
                .filter(img -> img.getImageType() == ImageType.MAIN)
                .findFirst()
                .map(AdminProductImageEntity::getImageUrl)
                .orElse(null);

        return ProductListResponseDto.builder()
                .productId(entity.getProductId())
                .productName(entity.getProductName())
                .brandName(entity.getProductBrand() != null ? entity.getProductBrand().getBrandName() : null)
                .productPrice(entity.getProductPrice())
                .discountedPrice(entity.getDiscountedPrice())
                .discountRate(entity.getDiscountRate())
                .stock(entity.getProductStock())
                .imageUrl(imageUrl)
                .build();
    }
}
