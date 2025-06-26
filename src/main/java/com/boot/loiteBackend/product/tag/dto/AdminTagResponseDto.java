package com.boot.loiteBackend.product.tag.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminTagResponseDto {
    private Long tagId;
    private String tagName;
    private String activeYn;
}
