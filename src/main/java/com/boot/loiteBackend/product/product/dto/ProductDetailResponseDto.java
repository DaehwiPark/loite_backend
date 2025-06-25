package com.boot.loiteBackend.product.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductDetailResponseDto {

    private Long productId;
    private String productName;
    private String productModelName;
    private String productSummary;
    private String productDescription;
    private BigDecimal productPrice;
    private BigDecimal productSupplyPrice;
    private Integer discountRate;
    private BigDecimal discountedPrice;
    private int productStock;
    private BigDecimal productDeliveryCharge;
    private BigDecimal productFreeDelivery;
    private String activeYn;
    private String deleteYn;
    private String recommendedYn;
    private Long productBrandId;
    private String brandName;
    private Long categoryId;
    private String categoryName;
    private List<TagDto> tags;
    private List<ImageDto> productImages;
    private List<ProductOptionDto> productOptions;

    @Getter
    @Setter
    public static class TagDto {
        private Long tagId;
        private String tagName;
    }

    @Getter
    @Setter
    public static class ImageDto {
        private String imageUrl;
        private String imageType;
        private int imageSortOrder;
        private String activeYn;
    }

    @Getter
    @Setter
    public static class ProductOptionDto {
        private String optionType;
        private String optionValue;
        private int optionAdditionalPrice;
        private int optionStock;
        private String optionStyleType;
        private String activeYn;
        private int optionSortOrder;
    }
}

