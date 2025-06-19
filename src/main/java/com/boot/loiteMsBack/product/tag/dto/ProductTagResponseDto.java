package com.boot.loiteMsBack.product.tag.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductTagResponseDto {
    private Long productTagId;
    private Long productId;
    private Long tagId;
}
