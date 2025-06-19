package com.boot.loiteMsBack.product.brand.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductBrandRequestDto {
    private Long brandId;
    private String brandName;
    private String brandOrigin;
    private String brandLogoUrl;
    private String brandDescription;
    private String activeYn;
}
