package com.boot.loiteBackend.support.faq.general.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "FAQ 수정 요청 DTO")
@Data
public class SupportFaqRequestDto {

    @Schema(description = "카테고리 아이디", example= "1")
    public Long faqCategoryId;

    @Schema(description = "수정할 질문", example = "비밀번호는 어떻게 변경하나요?")
    private String faqQuestion;

    @Schema(description = "수정할 답변", example = "마이페이지 > 보안설정에서 비밀번호를 변경할 수 있습니다.")
    private String faqAnswer;

    @Schema(description = "노출 여부", example= "Y")
    private String displayYn;

}
