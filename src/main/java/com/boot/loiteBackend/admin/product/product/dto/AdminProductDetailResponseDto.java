package com.boot.loiteBackend.admin.product.product.dto;

import com.boot.loiteBackend.admin.product.section.dto.AdminProductSectionResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AdminProductDetailResponseDto {
    @Schema(description = "상품 고유 ID", example = "1001")
    private Long productId;

    @Schema(description = "상품명", example = "모던 디자인 소파")
    private String productName;

    @Schema(description = "모델명", example = "MD-SF1234")
    private String productModelName;

    @Schema(description = "상품 요약 설명", example = "최신 트렌드의 북유럽 감성 3인 소파")
    private String productSummary;

    @Schema(description = "판매가", example = "299000.00")
    private BigDecimal productPrice;

    @Schema(description = "공급가", example = "190000.00")
    private BigDecimal productSupplyPrice;

    @Schema(description = "할인율(%)", example = "15")
    private Integer discountRate;

    @Schema(description = "할인가", example = "254150.00")
    private BigDecimal discountedPrice;

    @Schema(description = "기본 배송비", example = "3000.00")
    private BigDecimal productDeliveryCharge;

    @Schema(description = "무료 배송 기준 금액", example = "50000.00")
    private BigDecimal productFreeDelivery;

    @Schema(description = "상품 활성화 여부 (Y/N)", example = "Y")
    private String activeYn;

    @Schema(description = "상품 삭제 여부 (Y/N)", example = "N")
    private String deleteYn;

    @Schema(description = "메인페이지 노출 여부 (Y/N)", example = "Y")
    private String mainExposureYn;

    @Schema(description = "추천 상품 여부 (Y/N)", example = "N")
    private String recommendedYn;

    @Schema(description = "브랜드 ID", example = "10")
    private Long productBrandId;

    @Schema(description = "브랜드명", example = "로이테홈")
    private String brandName;

    @Schema(description = "카테고리 ID", example = "3002")
    private Long categoryId;

    @Schema(description = "카테고리명", example = "소파")
    private String categoryName;

    @Schema(description = "총 재고 수량", example = "100")
    private int productStock;

    private List<TagDto> tags;
    private List<ImageDto> productImages;
    private List<ProductOptionDto> productOptions;
    private List<AdminProductSectionResponseDto> productSections;

    @Getter
    @Setter
    public static class TagDto {
        @Schema(description = "태그 ID", example = "1")
        private Long tagId;

        @Schema(description = "태그명", example = "인기")
        private String tagName;
    }

    @Getter
    @Setter
    public static class ImageDto {
        @Schema(description = "이미지 URL", example = "https://cdn.loite.com/images/sofa01.jpg")
        private String imageUrl;

        @Schema(description = "이미지 구분 (예: THUMBNAIL, DETAIL)", example = "THUMBNAIL")
        private String imageType;

        @Schema(description = "이미지 정렬 순서", example = "1")
        private int imageSortOrder;

        @Schema(description = "활성 이미지 여부 (Y/N)", example = "Y")
        private String activeYn;
    }

    @Getter
    @Setter
    public static class ProductOptionDto {
        @Schema(description = "옵션 타입 (예: 색상, 용량)", example = "색상")
        private String optionType;

        @Schema(description = "옵션 값", example = "라이트 그레이")
        private String optionValue;

        @Schema(description = "색상 코드", example = "#D3D3D3")
        private String optionColorCode;

        @Schema(description = "옵션 추가 금액", example = "5000")
        private int optionAdditionalPrice;

        @Schema(description = "해당 옵션의 재고 수량", example = "20")
        private int optionStock;

        @Schema(description = "옵션 스타일 타입 (예: DROPDOWN, COLOR_PICKER)", example = "COLOR_PICKER")
        private String optionStyleType;

        @Schema(description = "옵션 활성화 여부 (Y/N)", example = "Y")
        private String activeYn;

        @Schema(description = "옵션 정렬 순서", example = "1")
        private int optionSortOrder;
    }
}


