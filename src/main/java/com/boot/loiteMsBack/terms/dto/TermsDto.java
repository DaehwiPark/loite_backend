package com.boot.loiteMsBack.terms.dto;

import com.boot.loiteMsBack.terms.entity.TermsEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "이용약관 DTO")
public class TermsDto {

    @Schema(description = "약관 ID", example = "1")
    private Long id;

    @Schema(description = "약관 제목", example = "2025년 6월 이용약관")
    private String title;

    @Schema(description = "약관 본문 (HTML)", example = "<p>이 약관은 ...</p>")
    private String content;

    @Schema(description = "약관 버전", example = "v1.0")
    private String version;

    @Schema(description = "생성일시", example = "2025-06-02T12:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시", example = "2025-06-02T12:05:00")
    private LocalDateTime updatedAt;

    public static TermsDto fromEntity(TermsEntity entity) {
        return TermsDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .version(entity.getVersion())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public TermsEntity toEntity() {
        return TermsEntity.builder()
                .title(this.title)
                .content(this.content)
                .version(this.version)
                .build(); // createdAt/updatedAt은 JPA에서 자동 처리
    }
}
