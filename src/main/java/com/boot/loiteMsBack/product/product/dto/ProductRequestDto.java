package com.boot.loiteMsBack.product.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProductRequestDto {
    private Long productId;
    private String productBrand;
    private String productSerialNumber;
    private String productName;
    private String productModelName;
    private String productSummary;
    private String productDescription;
    private String delYn;
    private String activeYn;
    private BigDecimal productPrice;
    private BigDecimal productSupplyPrice;
    private int productStock;
    private String recommendedYn;
    private BigDecimal productDeliveryCharge;
    private BigDecimal productFreeDelivery;
    private int viewCount;
    private int salesCount;
}
