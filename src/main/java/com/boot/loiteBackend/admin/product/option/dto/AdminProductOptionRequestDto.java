package com.boot.loiteBackend.admin.product.option.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminProductOptionRequestDto {
    @Schema(description = "옵션 ID", example = "1, 2, 3 ...")
    private Long optionId;

    @Schema(description = "상품 ID", example = "1, 2, 3 ...")
    private Long productId;

    @Schema(description = "옵션 타입", example = "색상, 사이즈 등")
    private String optionType;

    @Schema(description = "옵션값", example = "색상-화이트, 사이즈-15L")
    private String optionValue;

    @Schema(description = "색상코드", example = "#000000")
    private String optionColorCode;

    @Schema(description = "옵션 추가비용", example = "20,000₩")
    private int optionAdditionalPrice;

    @Schema(description = "옵션별 재고", example = "50")
    private int optionStock;

    @Schema(description = "옵션 스타일", example = "기본, 컬러피커, 드롭다운")
    private String optionStyleType;

    @Schema(description = "사용 여부", example = "Y, N")
    private String activeYn;

    @Schema(description = "옵션 정렬 순서", example = "index값으로 1, 2, 3 ...")
    private int optionSortOrder;
}
