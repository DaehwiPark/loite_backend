package com.boot.loiteBackend.admin.product.category.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminProductCategoryRequestDto {
    private Long categoryId;
    private Long categoryParentId;
    private String categoryName;
    private Integer categoryDepth;
    private int categorySortOrder;
    private String activeYn;
}

