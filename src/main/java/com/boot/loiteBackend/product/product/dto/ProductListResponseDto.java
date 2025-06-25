package com.boot.loiteBackend.product.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ProductListResponseDto {
    private LocalDateTime createdAt;
    private String activeYn;
    private String productName;
    private String brandName;
    private BigDecimal productPrice;
    private int productStock;
    private int viewCount;
    private int salesCount;
}
