package com.boot.loiteBackend.web.support.counsel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportCounselResponseDto {

    @Schema(description = "1:1 문의 ID", example = "101")
    private Long counselId;

    @Schema(description = "문의 제목", example = "배송 지연 문의")
    private String counselTitle;

    @Schema(description = "문의 상세 내용", example = "주문한 제품이 아직 배송되지 않았습니다.")
    private String counselContent;

    @Schema(description = "답변 받을 이메일 주소", example = "user@example.com")
    private String counselEmail;

    @Schema(description = "문의 상태", example = "WAITING", allowableValues = {"WAITING", "COMPLETE"})
    private String counselStatus;

    @Schema(description = "관리자의 답변 내용", example = "금일 중으로 배송 처리 예정입니다.")
    private String counselReplyContent;

    @Schema(description = "문의 등록일시", example = "2025-07-22T14:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "답변 등록일시", example = "2025-07-23T10:30:00")
    private LocalDateTime repliedAt;
}