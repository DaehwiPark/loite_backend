package com.boot.loiteBackend.support.counsel.dto;

import com.boot.loiteBackend.support.counsel.enums.CounselStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

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

    @Schema(description = "문의 제목", example = "배송 지연 문의")
    private String counselTitle;

    @Schema(description = "문의 상세 내용")
    private String counselContent;

    @Schema(description = "답변 수신 이메일", example = "user@example.com")
    private String counselEmail;

    @Schema(description = "문의 상태 (WAITING/COMPLETE)")
    private CounselStatus counselStatus;

    @Schema(description = "관리자 답변 내용")
    private String counselReplyContent;

    @Schema(description = "답변 작성 일시", example = "2025-06-17T12:34:56")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime counselRepliedAt;

    @Schema(description = "답변한 관리자 ID")
    private Long counselRepliedBy;

    @Schema(description = "문의 작성일시", example = "2025-06-17T10:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "문의 수정일시", example = "2025-06-17T11:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    @Schema(description = "삭제 여부 (Y: 삭제됨, N: 정상)")
    private String deleteYn;

    @Schema(description = "비밀글 여부 (Y: 비밀글, N: 일반글)")
    private String counselPrivateYn;

}
