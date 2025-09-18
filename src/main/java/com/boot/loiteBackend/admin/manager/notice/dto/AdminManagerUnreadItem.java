package com.boot.loiteBackend.admin.manager.notice.dto;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class AdminManagerUnreadItem {
    Long id;
    String title;
    LocalDateTime time; // publishedAt 우선, 없으면 createdAt
    int importance;     // 0: 일반, 1: 중요 등

    public static AdminManagerUnreadItem of(Long id, String title, LocalDateTime time, int importance) {
        return new AdminManagerUnreadItem(id, title, time, importance);
    }
}
