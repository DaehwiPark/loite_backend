package com.boot.loiteBackend.web.product.dto;

import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import com.boot.loiteBackend.admin.product.product.entity.AdminProductImageEntity;
import com.boot.loiteBackend.admin.product.product.enums.ImageType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ProductMainResponseDto {

    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private BigDecimal discountedPrice;
    private Integer discountRate;
    private String imageUrl;

    public static ProductMainResponseDto from(AdminProductEntity entity) {
        String imageUrl = entity.getProductImages().stream()
                .filter(img -> img.getImageType() == ImageType.MAIN)
                .findFirst()
                .map(AdminProductImageEntity::getImageUrl)
                .orElse(null);

        return ProductMainResponseDto.builder()
                .productId(entity.getProductId())
                .productName(entity.getProductName())
                .productPrice(entity.getProductPrice())
                .discountedPrice(entity.getDiscountedPrice())
                .discountRate(entity.getDiscountRate())
                .imageUrl(imageUrl)
                .build();
    }
}
