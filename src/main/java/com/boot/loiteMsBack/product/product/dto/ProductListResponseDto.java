package com.boot.loiteMsBack.product.product.dto;

import com.boot.loiteMsBack.product.option.dto.ProductOptionRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
