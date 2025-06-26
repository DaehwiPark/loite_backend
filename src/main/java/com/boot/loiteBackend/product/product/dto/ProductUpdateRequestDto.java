package com.boot.loiteBackend.product.product.dto;

import com.boot.loiteBackend.product.option.dto.ProductOptionRequestDto;
import com.boot.loiteBackend.product.section.dto.ProductSectionRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductUpdateRequestDto {
    private Long productId;
    private Long productBrandId;
    private Long categoryId;
    private String productName;
    private String productModelName;
    private String productSummary;
    private String deleteYn;
    private String activeYn;
    private String mainExposureYn;
    private BigDecimal productPrice;
    private BigDecimal productSupplyPrice;
    private Integer discountRate;
    private BigDecimal discountedPrice;
    private int productStock;
    private String recommendedYn;
    private BigDecimal productDeliveryCharge;
    private BigDecimal productFreeDelivery;
    private int viewCount;
    private int salesCount;

    private List<Long> giftIdList;
    private List<Long> tagIdList;
    List<ImageDto> productImages;
    List<ProductOptionRequestDto> productOptions;
    private List<ProductSectionRequestDto> sections;

    @Getter
    @Setter
    public static class ImageDto{
        private String imageUrl;
        private String imageType;
        private int imageSortOrder;
        private String activeYn;
    }
}
