package com.boot.loiteMsBack.support.counsel.dto;

import com.boot.loiteMsBack.support.counsel.entity.SupportCounselEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "1:1 문의 응답 DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupportCounselDto {

    @Schema(description = "문의 ID (PK)")
    private Long counselId;

    @Schema(description = "문의한 사용자 ID")
    private Long counselUserId;

    @Schema(description = "문의 제목")
    private String counselTitle;

    @Schema(description = "문의 내용")
    private String counselContent;

    @Schema(description = "답변 수신 이메일")
    private String counselEmail;

    @Schema(description = "문의 상태 (예: 대기, 처리중, 완료)")
    private String counselStatus;

    @Schema(description = "관리자 답변 내용")
    private String counselReplyContent;

    @Schema(description = "답변 작성 일시")
    private LocalDateTime counselRepliedAt;

    @Schema(description = "답변한 관리자 ID")
    private Long counselRepliedBy;

    @Schema(description = "문의 생성 일시")
    private LocalDateTime createdAt;

    @Schema(description = "문의 수정 일시")
    private LocalDateTime updatedAt;

    @Schema(description = "삭제 여부 (N=정상, Y=삭제됨)")
    private String delYn;
}
