package com.boot.loiteBackend.admin.support.resource.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "제품 리소스 DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminSupportResourceDto {

    @Schema(description = "resource ID", example = "1")
    private Long resourceId;

    @Schema(description = "제품 ID", example = "1001")
    private Long productId;

    @Schema(description = "제품 이름", example = "Air Conditioner")
    private String productName;

    @Schema(description = "모델명", example = "AC-1234X")
    private String productModelName;

    @Schema(description = "Uploaded file name", example = "ac_manual.pdf")
    private String resourceFileName;

    @Schema(description = "File URL path", example = "/uploads/resources/ac_manual.pdf")
    private String resourceFileUrl;

    @Schema(description = "File upload path", example = "C:/workspace/uploads/etc/uuid_filename.pdf")
    private String resourceFilePath;

    @Schema(description = "File size in bytes", example = "204800")
    private Long resourceFileSize;

    @Schema(description = "File type", example = "application/pdf")
    private String resourceFileType;

    @Schema(description = "노출 여부", example = "Y")
    private String displayYn;

    @Schema(description = "Created date", example = "2025-05-27T14:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Last updated date", example = "2025-05-27T15:00:00")
    private LocalDateTime updatedAt;
}
