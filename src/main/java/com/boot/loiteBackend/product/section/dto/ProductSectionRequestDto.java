package com.boot.loiteBackend.product.section.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSectionRequestDto {
    private Long sectionId;
    private String sectionType;
    private String sectionContent;
    private int sectionOrder;
    private String activeYn;
}

