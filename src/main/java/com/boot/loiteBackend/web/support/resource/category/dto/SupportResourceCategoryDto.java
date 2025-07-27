package com.boot.loiteBackend.web.support.resource.category.dto;

import com.boot.loiteBackend.admin.product.category.entity.AdminProductCategoryEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "제품 리소스 카테고리 DTO")
public class SupportResourceCategoryDto {

    @Schema(description = "카테고리 ID", example = "101")
    private Long categoryId;

    @Schema(description = "카테고리 이름", example = "가전제품")
    private String categoryName;

    public static SupportResourceCategoryDto from(AdminProductCategoryEntity entity) {
        return SupportResourceCategoryDto.builder()
                .categoryId(entity.getCategoryId())
                .categoryName(entity.getCategoryName())
                .build();
    }
}