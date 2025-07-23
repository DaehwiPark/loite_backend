package com.boot.loiteBackend.admin.product.brand.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminProductBrandResponseDto {
    @Schema(description = "브랜드ID", example = "1,2,3...")
    private Long brandId;

    @Schema(description = "브랜드명", example = "로이떼, HOMELIKE")
    private String brandName;
}
