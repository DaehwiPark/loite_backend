package com.boot.loiteBackend.news.general.mapper;

import com.boot.loiteBackend.news.general.dto.NewsDto;
import com.boot.loiteBackend.news.general.entity.NewsEntity;
import org.springframework.stereotype.Component;

@Component
public class NewsMapper {

    public NewsEntity toEntity(NewsDto dto) {
        return NewsEntity.builder()
                .newsTitle(dto.getNewsTitle())
                .newsContact(dto.getNewsContact())
                .newsThumbnailUrl(dto.getNewsThumbnailUrl())
                .newsContent(dto.getNewsContent())
                .build();
    }

    public NewsDto toDto(NewsEntity entity) {
        return NewsDto.builder()
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
