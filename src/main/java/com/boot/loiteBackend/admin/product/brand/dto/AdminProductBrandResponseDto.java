package com.boot.loiteBackend.admin.product.brand.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminProductBrandResponseDto {
    private Long brandId;
    private String brandName;
}
