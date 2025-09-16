package com.boot.loiteBackend.admin.manager.notice.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminManagerNoticeCreateRequest {
    private String title;
    private String contentMd;
    private Integer importance;   // 0/1
    private Boolean pinned;       // true/false
    private LocalDateTime expiresAt; // nullable
}
