package com.boot.loiteMsBack.support.resource.dto;

import com.boot.loiteMsBack.support.resource.entity.SupportResourceEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SupportResourceDto {

    @Schema(description = "Resource unique ID", example = "1")
    private Long resourceId;

    @Schema(description = "Product name", example = "Air Conditioner")
    private String resourceProductName;

    @Schema(description = "Model name", example = "AC-1234X")
    private String resourceModelName;

    @Schema(description = "Uploaded file name", example = "ac_manual.pdf")
    private String resourceFileName;

    @Schema(description = "File URL path", example = "/uploads/resources/ac_manual.pdf")
    private String resourceFileUrl;

    @Schema(description = "File upload path", example = "C:/workspace/uploads/etc/uuid_filename.pdf)")
    private String resourceFilePath;

    @Schema(description = "File size in bytes", example = "204800")
    private Long resourceFileSize;

    @Schema(description = "File type", example = "application/pdf")
    private String resourceFileType;

    @Schema(description = "Created date", example = "2025-05-27T14:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Last updated date", example = "2025-05-27T15:00:00")
    private LocalDateTime updatedAt;

    public SupportResourceDto(SupportResourceEntity entity) {
        this.resourceId = entity.getResourceId();
        this.resourceProductName = entity.getResourceProductName();
        this.resourceModelName = entity.getResourceModelName();
        this.resourceFileName = entity.getResourceFileName();
        this.resourceFileUrl = entity.getResourceFileUrl();
        this.resourceFilePath = entity.getResourceFilePath();
        this.resourceFileSize = entity.getResourceFileSize();
        this.resourceFileType = entity.getResourceFileType();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}
