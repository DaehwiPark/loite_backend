package com.boot.loiteBackend.product.section.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AdminProductSectionResponseDto {
    private Long sectionId;
    private String sectionType;
    private String sectionContent;
    private int sectionOrder;
}