package com.boot.loiteBackend.admin.news.general.service;

import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.admin.news.general.dto.AdminNewsDto;
import com.boot.loiteBackend.admin.news.general.entity.AdminNewsEntity;
import com.boot.loiteBackend.admin.news.general.error.AdminNewsErrorCode;
import com.boot.loiteBackend.admin.news.general.mapper.AdminNewsMapper;
import com.boot.loiteBackend.admin.news.general.repository.AdminNewsRepository;
import com.boot.loiteBackend.global.util.file.FileService;
import com.boot.loiteBackend.global.util.file.FileUploadResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AdminNewsServiceImpl implements AdminNewsService {

    private final AdminNewsRepository adminNewsRepository;
    private final FileService fileService;
    private final AdminNewsMapper adminNewsMapper;

    private final String uploadCategory = "etc";

    @Override
    @Transactional
    public AdminNewsDto createNews(AdminNewsDto dto, MultipartFile thumbnail) {
        if (thumbnail == null || thumbnail.isEmpty()) {
            throw new CustomException(AdminNewsErrorCode.INVALID_THUMBNAIL);
        }

        FileUploadResult uploadResult;
        try {
            uploadResult = fileService.save(thumbnail, uploadCategory);
        } catch (Exception e) {
            throw new CustomException(AdminNewsErrorCode.THUMBNAIL_UPLOAD_FAILED);
        }

        try {
            dto.setNewsThumbnailUrl(uploadResult.getUrlPath());

            AdminNewsEntity entity = adminNewsMapper.toEntity(dto);
            AdminNewsEntity saved = adminNewsRepository.save(entity);
            return adminNewsMapper.toDto(saved);
        } catch (Exception e) {
            throw new CustomException(AdminNewsErrorCode.SAVE_FAILED);
        }
    }

    @Override
    @Transactional
    public AdminNewsDto updateNews(Long id, AdminNewsDto dto) {
        AdminNewsEntity entity = adminNewsRepository.findById(id)
                .orElseThrow(() -> new CustomException(AdminNewsErrorCode.NOT_FOUND));

        entity.setNewsTitle(dto.getNewsTitle());
        entity.setNewsContent(dto.getNewsContent());
        entity.setNewsThumbnailUrl(dto.getNewsThumbnailUrl());
        entity.setNewsContact(dto.getNewsContact());

        AdminNewsEntity updated = adminNewsRepository.save(entity);
        return adminNewsMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deleteNews(Long id) {
        if (!adminNewsRepository.existsById(id)) {
            throw new CustomException(AdminNewsErrorCode.NOT_FOUND);
        }
        adminNewsRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminNewsDto> getAllNews() {
        return adminNewsRepository.findAll()
                .stream()
                .map(adminNewsMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AdminNewsDto getNewsById(Long id) {
        AdminNewsEntity entity = adminNewsRepository.findById(id)
                .orElseThrow(() -> new CustomException(AdminNewsErrorCode.NOT_FOUND));
        return adminNewsMapper.toDto(entity);
    }
}
