package com.boot.loiteBackend.web.support.resource.general.service;

import com.boot.loiteBackend.admin.product.product.entity.AdminProductEntity;
import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.web.support.resource.general.dto.SupportResourceDto;
import com.boot.loiteBackend.web.support.resource.general.entity.SupportResourceEntity;
import com.boot.loiteBackend.web.support.resource.general.error.ResourceErrorCode;
import com.boot.loiteBackend.web.support.resource.general.repository.SupportResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class SupportResourceServiceImpl implements SupportResourceService {

    private final SupportResourceRepository resourceRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<SupportResourceDto> getManuals(String keyword, Long categoryId, Pageable pageable) {
        String trimmedKeyword = StringUtils.hasText(keyword) ? keyword.trim() : null;
        return resourceRepository.searchWithKeywordAndCategory(categoryId, trimmedKeyword, pageable)
                .map(this::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Resource> fileDownload(Long resourceId) {
        SupportResourceEntity entity = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new CustomException(ResourceErrorCode.DB_RESOURCE_NOT_FOUND));

        try {
            Path filePath = Paths.get(entity.getResourceFilePath()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new CustomException(ResourceErrorCode.FILE_NOT_FOUND);
            }

            String encodedFileName = URLEncoder.encode(entity.getResourceFileName(), StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
                    .body(resource);

        } catch (MalformedURLException e) {
            throw new CustomException(ResourceErrorCode.FILE_UPLOAD_FAILED);
        }
    }


    private SupportResourceDto toDto(SupportResourceEntity entity) {
        if (entity == null) return null;

        AdminProductEntity product = entity.getProduct();
        String categoryName = null;

        if (product != null && product.getProductCategory() != null) {
            categoryName = product.getProductCategory().getCategoryName();
        }

        return SupportResourceDto.builder()
                .resourceId(entity.getResourceId())
                .productId(product != null ? product.getProductId() : null)
                .productName(product != null ? product.getProductName() : null)
                .productModelName(product != null ? product.getProductModelName() : null)
                .categoryName(categoryName)
                .resourceFileName(entity.getResourceFileName())
                .resourceFilePath(entity.getResourceFilePath())
                .resourceFileUrl(entity.getResourceFileUrl())
                .resourceFileSize(entity.getResourceFileSize())
                .resourceFileType(entity.getResourceFileType())
                .displayYn(entity.getDisplayYn())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
