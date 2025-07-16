package com.boot.loiteBackend.web.support.resource.category.dto;

import com.boot.loiteBackend.admin.product.category.entity.AdminProductCategoryEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SupportResourceCategoryDto {
    private Long categoryId;
    private String categoryName;

    public static SupportResourceCategoryDto from(AdminProductCategoryEntity entity) {
        return SupportResourceCategoryDto.builder()
                .categoryId(entity.getCategoryId())
                .categoryName(entity.getCategoryName())
                .build();
    }
}
