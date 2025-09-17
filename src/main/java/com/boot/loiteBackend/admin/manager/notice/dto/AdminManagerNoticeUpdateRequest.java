package com.boot.loiteBackend.admin.manager.notice.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AdminManagerNoticeUpdateRequest(
        @NotBlank String title,
        @NotBlank String contentMd,
        @NotNull Integer importance, // 0 = 일반, 1 = 중요 (정수 사용)
        boolean pinned,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime expiresAt // null 허용
) {}