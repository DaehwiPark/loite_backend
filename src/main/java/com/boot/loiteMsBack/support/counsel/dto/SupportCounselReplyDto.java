package com.boot.loiteMsBack.support.counsel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "1:1 문의 답변 등록/수정 요청 DTO")
@Data
@NoArgsConstructor
public class SupportCounselReplyDto {

    @Schema(description = "답변 내용", example = "고객님 불편을 드려 죄송합니다. 해당 건은 조치 중입니다.")
    private String replyContent;

    @Schema(description = "답변을 등록한 관리자 ID", example = "2")
    private Long repliedBy;
}
