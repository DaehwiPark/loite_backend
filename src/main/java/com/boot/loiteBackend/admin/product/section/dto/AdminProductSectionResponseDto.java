package com.boot.loiteBackend.admin.product.section.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AdminProductSectionResponseDto {

    @Schema(description = "섹션 고유 ID", example = "301")
    private Long sectionId;

    @Schema(description = "섹션 타입 (버튼명 또는 탭 제목으로 사용)", example = "디자인")
    private String sectionType;

    @Schema(description = "섹션 콘텐츠 (HTML 또는 이미지 포함 설명)", example = "<p>감각적인 디자인으로 어떤 공간에도 잘 어울립니다.</p><img src='https://cdn.loite.com/images/detail01.jpg' />")
    private String sectionContent;

    @Schema(description = "섹션 정렬 순서", example = "1")
    private int sectionOrder;
}
