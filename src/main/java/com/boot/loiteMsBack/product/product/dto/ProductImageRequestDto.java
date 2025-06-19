package com.boot.loiteMsBack.product.product.dto;

import com.boot.loiteMsBack.product.product.entity.ProductEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductImageRequestDto {
    private Long imageId;
    private Long productId;
    private String imageUrl;
    private String imageType;
    private String imagePath;
    private int imageSortOrder;
    private String activeYn;
}
