package com.boot.loiteBackend.admin.manager.notice.dto;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class AdminManagerUnreadItem {
    Long id;
    String title;
    LocalDateTime time;  // publishedAt 우선, 없으면 createdAt
    Integer importance;  // int → Integer 로 변경

    public static AdminManagerUnreadItem of(Long id, String title, LocalDateTime time, Integer importance) {
        return new AdminManagerUnreadItem(id, title, time, importance);
    }

    // 선택: null 안전하게 쓰고 싶으면 헬퍼 하나 추가
    public int importanceOrZero() { return importance != null ? importance : 0; }
}
