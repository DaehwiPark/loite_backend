package com.boot.loiteBackend.admin.product.tag.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminProductTagResponseDto {

    @Schema(description = "상품 태그 매핑 ID (수정 시 필요, 등록 시 null)", example = "501")
    private Long productTagId;

    @Schema(description = "연결할 상품 ID", example = "1001")
    private Long productId;

    @Schema(description = "연결할 태그 ID", example = "3")
    private Long tagId;
}
