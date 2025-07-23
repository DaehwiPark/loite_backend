package com.boot.loiteBackend.admin.product.section.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminProductSectionRequestDto {

    @Schema(description = "섹션 ID (수정 시 필요, 신규 등록 시 null)", example = "301")
    private Long sectionId;

    @Schema(description = "섹션 타입 (버튼명 또는 섹션 제목으로 사용됨)", example = "디자인")
    private String sectionType;

    @Schema(description = "섹션 내용 (HTML 또는 텍스트+이미지 조합)", example = "<p>감각적인 디자인으로 어떤 공간에도 잘 어울립니다.</p><img src='https://cdn.loite.com/images/detail01.jpg' />")
    private String sectionContent;

    @Schema(description = "섹션 정렬 순서", example = "1")
    private int sectionOrder;

    @Schema(description = "활성화 여부 (Y/N)", example = "Y")
    private String activeYn;
}


