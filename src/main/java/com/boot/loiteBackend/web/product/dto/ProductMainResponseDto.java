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
}
