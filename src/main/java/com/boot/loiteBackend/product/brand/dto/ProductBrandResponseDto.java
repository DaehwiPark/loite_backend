package com.boot.loiteBackend.product.brand.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductBrandResponseDto {
    private Long brandId;
    private String brandName;
}
