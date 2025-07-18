package com.boot.loiteBackend.web.product.dto;

import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import com.boot.loiteBackend.admin.product.product.entity.AdminProductImageEntity;
import com.boot.loiteBackend.admin.product.product.enums.ImageType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ProductListResponseDto {

    private Long productId;
    private String productName;
    private String brandName;
    private BigDecimal productPrice;
    private BigDecimal discountedPrice;
    private Integer discountRate;
    private String imageUrl;
    private Integer stock;
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
