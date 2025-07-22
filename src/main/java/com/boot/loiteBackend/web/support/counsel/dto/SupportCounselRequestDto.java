package com.boot.loiteBackend.web.support.counsel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportCounselRequestDto {

    @Schema(description = "문의 제목", example = "배송 지연 문의")
    private String counselTitle;

    @Schema(description = "문의 상세 내용", example = "주문한 제품이 5일 이상 배송되지 않고 있습니다.")
    private String counselContent;

    @Schema(description = "답변 받을 이메일 주소", example = "user@example.com")
    private String counselEmail;

    @Schema(description = "비밀글 여부 (Y: 비밀글, N: 일반글)", example = "N", defaultValue = "N")
    private String counselPrivateYn;

    @Schema(description = "비밀글 비밀번호 (비밀글일 경우 필수)", example = "securePassword123!")
    private String counselPrivatePassword;
}