package com.boot.loiteBackend.admin.product.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminProductImageRequestDto {
    private Long imageId;
    private Long productId;
    private String imageUrl;
    private String imageType;
    private String imagePath;
    private int imageSortOrder;
    private String activeYn;
}
