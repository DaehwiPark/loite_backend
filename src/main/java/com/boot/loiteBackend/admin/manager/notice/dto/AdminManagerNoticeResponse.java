package com.boot.loiteBackend.admin.manager.notice.dto;

import com.boot.loiteBackend.domain.manager.notice.entity.AdminManagerNoticeAttachment;
import com.boot.loiteBackend.domain.manager.notice.entity.AdminManagerNoticeEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Builder
public class AdminManagerNoticeResponse {
    private Long id;
    private String title;
    private String contentMd;
    private Integer importance;
    private Boolean pinned;
    private String status;
    private LocalDateTime publishedAt;
    private LocalDateTime expiresAt;
    private List<AttachmentDto> attachments;

    @Getter @Builder
    public static class AttachmentDto {
        private Long id;
        private String url;
        private String name;
        private Long sizeBytes;
    }

    public static AdminManagerNoticeResponse of(AdminManagerNoticeEntity n, List<AdminManagerNoticeAttachment> atts) {
        return AdminManagerNoticeResponse.builder()
                .id(n.getId())
                .title(n.getTitle())
                .contentMd(n.getContentMd())
                .importance(n.getImportance())
                .pinned(n.getPinned())
                .status(n.getStatus().name())
                .publishedAt(n.getPublishedAt())
                .expiresAt(n.getExpiresAt())
                .attachments(atts.stream().map(a ->
                        AttachmentDto.builder()
                                .id(a.getId())
                                .url(a.getFileUrl())
                                .name(a.getOriginalName())
                                .sizeBytes(a.getFileSizeBytes())
                                .build()
                ).toList())
                .build();
    }
}
