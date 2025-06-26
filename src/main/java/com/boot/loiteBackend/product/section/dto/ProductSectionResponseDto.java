package com.boot.loiteBackend.product.section.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ProductSectionResponseDto {
    private Long sectionId;
    private String sectionType;
    private String sectionContent;
    private int sectionOrder;
}