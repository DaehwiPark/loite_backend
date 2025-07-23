package com.boot.loiteBackend.admin.product.product.dto;

import com.boot.loiteBackend.admin.product.option.dto.AdminProductOptionRequestDto;
import com.boot.loiteBackend.admin.product.section.dto.AdminProductSectionRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AdminProductRequestDto {

    @Schema(description = "상품 ID (수정 시 필요, 신규 등록 시 null)", example = "1001")
    private Long productId;

    @Schema(description = "브랜드 ID", example = "10")
    private Long productBrandId;

    @Schema(description = "카테고리 ID", example = "2001")
    private Long categoryId;

    @Schema(description = "상품명", example = "프리미엄 원목 테이블")
    private String productName;

    @Schema(description = "모델명", example = "WTB-2025")
    private String productModelName;

    @Schema(description = "상품 요약 설명", example = "심플하고 고급스러운 원목 테이블")
    private String productSummary;

    @Schema(description = "상품 삭제 여부 (Y/N)", example = "N")
    private String deleteYn;

    @Schema(description = "상품 활성화 여부 (Y/N)", example = "Y")
    private String activeYn;

    @Schema(description = "메인페이지 노출 여부 (Y/N)", example = "Y")
    private String mainExposureYn;

    @Schema(description = "판매가", example = "299000.00")
    private BigDecimal productPrice;

    @Schema(description = "공급가", example = "210000.00")
    private BigDecimal productSupplyPrice;

    @Schema(description = "할인율 (%)", example = "10")
    private Integer discountRate;

    @Schema(description = "할인가", example = "269100.00")
    private BigDecimal discountedPrice;

    @Schema(description = "추천 상품 여부 (Y/N)", example = "N")
    private String recommendedYn;

    @Schema(description = "기본 배송비", example = "3000.00")
    private BigDecimal productDeliveryCharge;

    @Schema(description = "무료 배송 기준 금액", example = "50000.00")
    private BigDecimal productFreeDelivery;

    @Schema(description = "조회수", example = "120")
    private int viewCount;

    @Schema(description = "누적 판매 수량", example = "35")
    private int salesCount;

    @Schema(description = "사은품 ID 목록", example = "[1, 3, 5]")
    private List<Long> giftIdList;

    @Schema(description = "태그 ID 목록", example = "[2, 4, 6]")
    private List<Long> tagIdList;

    private List<ImageDto> productImages;
    private List<AdminProductOptionRequestDto> productOptions;
    private List<AdminProductSectionRequestDto> sections;

    @Getter
    @Setter
    public static class ImageDto {
        @Schema(description = "이미지 URL", example = "https://cdn.loite.com/images/product/wood-table.jpg")
        private String imageUrl;

        @Schema(description = "이미지 구분 (예: THUMBNAIL, DETAIL)", example = "THUMBNAIL")
        private String imageType;

        @Schema(description = "이미지 정렬 순서", example = "1")
        private int imageSortOrder;

        @Schema(description = "활성화 여부 (Y/N)", example = "Y")
        private String activeYn;
    }
}

