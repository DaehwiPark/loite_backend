package com.boot.loiteBackend.policy.dto;

import com.boot.loiteBackend.policy.entity.PolicyEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "개인정보처리방침 DTO")
public class PolicyDto {

    @Schema(description = "정책 ID", example = "1")
    private Long policyId;

    @Schema(description = "정책 제목", example = "개인정보 처리방침 동의서")
    private String policyTitle;

    @Schema(description = "정책 본문 (HTML 가능)", example = "<p>귀하의 개인정보는 다음과 같이 처리됩니다.</p>")
    private String policyContent;

    @Schema(description = "노출여부 (노출:Y/비노출:N")
    private String displayYn;

    @Schema(description = "생성일시", example = "2024-06-01T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시", example = "2024-06-02T12:30:00")
    private LocalDateTime updatedAt;

    public static PolicyDto fromEntity(PolicyEntity entity) {
        return PolicyDto.builder()
                .policyId(entity.getPolicyId())
                .policyTitle(entity.getPolicyTitle())
                .policyContent(entity.getPolicyContent())
                .displayYn(entity.getDisplayYn())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public PolicyEntity toEntity() {
        return PolicyEntity.builder()
                .policyTitle(this.policyTitle)
                .policyContent(this.policyContent)
                .displayYn(this.displayYn)
                .build();
    }
}
