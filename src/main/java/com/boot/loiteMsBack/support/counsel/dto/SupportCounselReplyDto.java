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

    @Schema(description = "비밀글 여부 (Y: 비밀글, N: 일반글)", example = "N")
    private String counselPrivateYn;

    @Schema(description = "비밀글 비밀번호 (암호화된 값)", example = "$2a$10$xxxxxxxxxxxxxxxxxxxx")
    private String counselSecretPassword;
}
