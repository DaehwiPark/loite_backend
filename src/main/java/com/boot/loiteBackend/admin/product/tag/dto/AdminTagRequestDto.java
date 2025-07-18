package com.boot.loiteBackend.admin.product.tag.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminTagRequestDto {
    private Long tagId;
    private String tagName;
    private String activeYn;
}
