package com.boot.loiteBackend.admin.product.tag.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductTagRequestDto {
    private Long productTagId;
    private Long productId;
    private Long tagId;
}
