package com.boot.loiteBackend.admin.product.brand.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminProductBrandRequestDto {
    @Schema(description = "브랜드ID", example = "1,2,3...")
    private Long brandId;

    @Schema(description = "브랜드명", example = "로이떼, HOMELIKE")
    private String brandName;

    @Schema(description = "국가", example = "한국, 중국")
    private String brandOrigin;

    @Schema(description = "브랜드 로고", example = "url")
    private String brandLogoUrl;

    @Schema(description = "브랜드 설명", example = "무슨 무슨 브랜드 입니다.")
    private String brandDescription;

    @Schema(description = "사용 여부", example = "Y,N")
    private String activeYn;
}
