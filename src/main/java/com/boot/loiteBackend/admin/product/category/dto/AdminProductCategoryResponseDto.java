package com.boot.loiteBackend.admin.product.category.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AdminProductCategoryResponseDto {
    private Long categoryId;
    private Long ParentId;
    private String categoryName;
    private Integer categoryDepth;
    private int categorySortOrder;
    private String activeYn;
    private String deleteYn;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<AdminProductCategoryResponseDto> children = new ArrayList<>();
}
