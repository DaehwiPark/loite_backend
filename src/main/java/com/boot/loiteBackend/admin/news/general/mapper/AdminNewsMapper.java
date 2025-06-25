package com.boot.loiteBackend.admin.news.general.mapper;

import com.boot.loiteBackend.admin.news.general.dto.AdminNewsDto;
import com.boot.loiteBackend.admin.news.general.entity.AdminNewsEntity;
import org.springframework.stereotype.Component;

@Component
public class AdminNewsMapper {

    public AdminNewsEntity toEntity(AdminNewsDto dto) {
        return AdminNewsEntity.builder()
                .newsTitle(dto.getNewsTitle())
                .newsContact(dto.getNewsContact())
                .newsThumbnailUrl(dto.getNewsThumbnailUrl())
                .newsContent(dto.getNewsContent())
                .build();
    }

    public AdminNewsDto toDto(AdminNewsEntity entity) {
        return AdminNewsDto.builder()
                .newsId(entity.getNewsId())
                .newsTitle(entity.getNewsTitle())
                .newsContact(entity.getNewsContact())
                .newsThumbnailUrl(entity.getNewsThumbnailUrl())
                .newsContent(entity.getNewsContent())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
