package com.boot.loiteBackend.admin.product.tag.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminTagRequestDto {

    @Schema(description = "태그 ID (수정 시 필요, 등록 시 null)", example = "3")
    private Long tagId;

    @Schema(description = "태그명", example = "인기")
    private String tagName;

    @Schema(description = "활성화 여부 (Y/N)", example = "Y")
    private String activeYn;
}

