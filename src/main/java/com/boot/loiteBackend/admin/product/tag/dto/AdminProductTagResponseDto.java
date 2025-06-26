package com.boot.loiteBackend.product.tag.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminProductTagResponseDto {
    private Long productTagId;
    private Long productId;
    private Long tagId;
}
