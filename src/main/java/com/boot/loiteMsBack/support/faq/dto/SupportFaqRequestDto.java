package com.boot.loiteMsBack.support.faq.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "FAQ 수정 요청 DTO")
@Setter
@Getter
public class SupportFaqRequestDto {

    @Schema(description = "수정할 질문", example = "비밀번호는 어떻게 변경하나요?")
    private String faqQuestion;

    @Schema(description = "수정할 답변", example = "마이페이지 > 보안설정에서 비밀번호를 변경할 수 있습니다.")
    private String faqAnswer;
}
