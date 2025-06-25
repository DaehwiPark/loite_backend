package com.boot.loiteBackend.news.general.service;

import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.news.general.dto.NewsDto;
import com.boot.loiteBackend.news.general.entity.NewsEntity;
import com.boot.loiteBackend.news.general.error.NewsErrorCode;
import com.boot.loiteBackend.news.general.mapper.NewsMapper;
import com.boot.loiteBackend.news.general.repository.NewsRepository;
import com.boot.loiteBackend.util.file.FileService;
import com.boot.loiteBackend.util.file.FileUploadResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final FileService fileService;
    private final NewsMapper newsMapper;

    private final String uploadCategory = "etc";

    @Override
    @Transactional
    public NewsDto createNews(NewsDto dto, MultipartFile thumbnail) {
        if (thumbnail == null || thumbnail.isEmpty()) {
            throw new CustomException(NewsErrorCode.INVALID_THUMBNAIL);
        }

        FileUploadResult uploadResult;
        try {
            uploadResult = fileService.save(thumbnail, uploadCategory);
        } catch (Exception e) {
            throw new CustomException(NewsErrorCode.THUMBNAIL_UPLOAD_FAILED);
        }

        try {
            dto.setNewsThumbnailUrl(uploadResult.getUrlPath());

            NewsEntity entity = newsMapper.toEntity(dto);
            NewsEntity saved = newsRepository.save(entity);
            return newsMapper.toDto(saved);
        } catch (Exception e) {
            throw new CustomException(NewsErrorCode.SAVE_FAILED);
        }
    }

    @Override
    @Transactional
    public NewsDto updateNews(Long id, NewsDto dto) {
        NewsEntity entity = newsRepository.findById(id)
                .orElseThrow(() -> new CustomException(NewsErrorCode.NOT_FOUND));

        entity.setNewsTitle(dto.getNewsTitle());
        entity.setNewsContent(dto.getNewsContent());
        entity.setNewsThumbnailUrl(dto.getNewsThumbnailUrl());
        entity.setNewsContact(dto.getNewsContact());

        NewsEntity updated = newsRepository.save(entity);
        return newsMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deleteNews(Long id) {
        if (!newsRepository.existsById(id)) {
            throw new CustomException(NewsErrorCode.NOT_FOUND);
        }
        newsRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsDto> getAllNews() {
        return newsRepository.findAll()
                .stream()
                .map(newsMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public NewsDto getNewsById(Long id) {
        NewsEntity entity = newsRepository.findById(id)
                .orElseThrow(() -> new CustomException(NewsErrorCode.NOT_FOUND));
        return newsMapper.toDto(entity);
    }
}
