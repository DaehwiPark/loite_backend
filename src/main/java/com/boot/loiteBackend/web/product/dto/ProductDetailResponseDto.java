package com.boot.loiteBackend.web.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class ProductDetailResponseDto {

    @Schema(description = "상품 ID", example = "1001")
    private Long productId;

    @Schema(description = "브랜드명", example = "로이테가구")
    private String brandName;

    @Schema(description = "상품명", example = "프리미엄 원목 테이블")
    private String productName;

    @Schema(description = "썸네일 이미지 리스트 (대표 포함)", example = "[\"https://cdn.loite.com/images/p1_main.jpg\", \"https://cdn.loite.com/images/p1_sub.jpg\"]")
    private List<String> thumbnailImages;

    @Schema(description = "상품 옵션 리스트", implementation = ProductOptionDto.class)
    private List<ProductOptionDto> options;

    @Schema(description = "선택 가능한 사은품 리스트", implementation = ProductGiftDto.class)
    private List<ProductGiftDto> gifts;

    @Schema(description = "상품 정가", example = "299000.00")
    private BigDecimal productPrice;

    @Schema(description = "할인가", example = "269100.00")
    private BigDecimal discountedPrice;

    @Schema(description = "할인율 (%)", example = "10")
    private Integer discountRate;

    @Schema(description = "배송비", example = "3000.00")
    private BigDecimal deliveryCharge;

    @Schema(description = "모델명", example = "WTB-2025")
    private String modelName;

    @Schema(description = "상품 코드 (고유값)", example = "P202507230001")
    private String productCode;

    @Schema(description = "총 재고 수량", example = "35")
    private Integer productStock;

    @Schema(description = "상품 상세 섹션 리스트", implementation = ProductSectionDto.class)
    private List<ProductSectionDto> sections;

    // 옵션
    @Getter
    @Builder
    public static class ProductOptionDto {
        @Schema(description = "옵션 ID", example = "301")
        private Long optionId;

        @Schema(description = "옵션 타입 (예: 색상, 용량)", example = "색상")
        private String optionType;

        @Schema(description = "옵션 값", example = "내추럴 우드")
        private String optionValue;

        @Schema(description = "색상 코드 (선택사항)", example = "#D3D3D3")
        private String optionColorCode;

        @Schema(description = "옵션 추가 금액", example = "5000")
        private int optionAdditionalPrice;

        @Schema(description = "해당 옵션 재고 수량", example = "10")
        private int optionStock;

        @Schema(description = "품절 여부", example = "false")
        private Boolean soldOutYn;
    }

    // 사은품
    @Getter
    @Builder
    public static class ProductGiftDto {
        @Schema(description = "상품과 사은품 고유 연결 ID", example = "11")
        private Long productGiftId;

        @Schema(description = "사은품 ID", example = "11")
        private Long giftId;

        @Schema(description = "사은품명", example = "코튼 러그")
        private String giftName;

        @Schema(description = "사은품 이미지", example = "example")
        private String giftImageUrl;

        @Schema(description = "사은품 재고 수량", example = "5")
        private int giftStock;

        @Schema(description = "품절 여부", example = "false")
        private Boolean soldOutYn;
    }

    // 상세 섹션
    @Getter
    @Builder
    public static class ProductSectionDto {
        @Schema(description = "섹션 타입 (버튼 또는 탭 제목)", example = "디자인")
        private String sectionType;

        @Schema(description = "섹션 내용 (HTML 또는 텍스트+이미지 조합)", example = "<p>감각적인 디자인의 원목이 돋보입니다.</p>")
        private String content;

        @Schema(description = "섹션 정렬 순서", example = "1")
        private int sectionOrder;
    }
}

