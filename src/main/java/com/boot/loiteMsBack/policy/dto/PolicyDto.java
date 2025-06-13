package com.boot.loiteMsBack.policy.dto;

import com.boot.loiteMsBack.policy.entity.PolicyEntity;
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
    private Long id;

    @Schema(description = "정책 제목", example = "개인정보 처리방침 동의서")
    private String title;

    @Schema(description = "정책 본문 (HTML 가능)", example = "<p>귀하의 개인정보는 다음과 같이 처리됩니다.</p>")
    private String content;

    @Schema(description = "정책 버전", example = "v1.0")
    private String version;

    @Schema(description = "현재 사용 여부", example = "true")
    private Boolean isActive;

    @Schema(description = "생성일시", example = "2024-06-01T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시", example = "2024-06-02T12:30:00")
    private LocalDateTime updatedAt;

    public static PolicyDto fromEntity(PolicyEntity entity) {
        return PolicyDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .version(entity.getVersion())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public PolicyEntity toEntity() {
        return PolicyEntity.builder()
                .title(this.title)
                .content(this.content)
                .version(this.version)
                .isActive(this.isActive != null ? this.isActive : true)
                .build();
    }
}
