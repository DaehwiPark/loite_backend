package com.boot.loiteBackend.admin.product.brand.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminProductBrandRequestDto {
    private Long brandId;
    private String brandName;
    private String brandOrigin;
    private String brandLogoUrl;
    private String brandDescription;
    private String activeYn;
}
