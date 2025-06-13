package com.boot.loiteMsBack.product.tag.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TagRequestDto {
    private Long tagId;
    private String tagName;
    private String activeYn;
}
