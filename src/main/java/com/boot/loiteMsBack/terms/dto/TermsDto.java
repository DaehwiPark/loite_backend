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
    private Long termsId;

    @Schema(description = "약관 제목", example = "2025년 6월 이용약관")
    private String termsTitle;

    @Schema(description = "약관 본문 (HTML)", example = "<p>이 약관은 ...</p>")
    private String termsContent;

    @Schema(description = "노출여부 (노출:Y / 비노출:N)", example = "Y")
    private String displayYn;

    @Schema(description = "생성일시", example = "2025-06-02T12:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시", example = "2025-06-02T12:05:00")
    private LocalDateTime updatedAt;

    public static TermsDto fromEntity(TermsEntity entity) {
        return TermsDto.builder()
                .termsId(entity.getTermsId())
                .termsTitle(entity.getTermsTitle())
                .termsContent(entity.getTermsContent())
                .displayYn(entity.getDisplayYn())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public TermsEntity toEntity() {
        return TermsEntity.builder()
                .termsTitle(this.termsTitle)
                .termsContent(this.termsContent)
                .displayYn(this.displayYn)
                .build();
    }
}
