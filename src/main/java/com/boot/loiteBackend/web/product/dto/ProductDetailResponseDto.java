package com.boot.loiteBackend.web.product.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class ProductDetailResponseDto {

    private Long productId;
    private String brandName;
    private String productName;

    private List<String> thumbnailImages;

    private List<ProductOptionDto> options;
    private List<ProductGiftDto> gifts;

    private BigDecimal productPrice;
    private BigDecimal discountedPrice;
    private Integer discountRate;
    private BigDecimal deliveryCharge;

    private String modelName;
    private String productCode;
    private Integer productStock;


    private List<ProductSectionDto> sections;

    // 옵션
    @Getter
    @Builder
    public static class ProductOptionDto {
        private Long optionId;
        private String optionType;
        private String optionValue;
        private String optionColorCode;
        private int optionAdditionalPrice;
        private int optionStock;
        private Boolean soldOutYn;
    }

    // 사은품
    @Getter
    @Builder
    public static class ProductGiftDto {
        private Long giftId;
        private String giftName;
        private int giftStock;
        private Boolean soldOutYn;
    }

    // 상세 섹션
    @Getter
    @Builder
    public static class ProductSectionDto {
        private String sectionType;
        private String content;
        private int sectionOrder;
    }
}
