package com.boot.loiteBackend.support.resource.service;

import com.boot.loiteBackend.config.FileStorageProperties;
import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.product.product.dto.AdminProductSummaryDto;
import com.boot.loiteBackend.product.product.entity.AdminProductEntity;
import com.boot.loiteBackend.product.product.repository.AdminProductRepository;
import com.boot.loiteBackend.support.resource.dto.SupportResourceDto;
import com.boot.loiteBackend.support.resource.dto.SupportResourceRequestDto;
import com.boot.loiteBackend.support.resource.entity.SupportResourceEntity;
import com.boot.loiteBackend.support.resource.error.ResourceErrorCode;
import com.boot.loiteBackend.support.resource.mapper.SupportResourceMapper;
import com.boot.loiteBackend.support.resource.repository.SupportResourceRepository;
import com.boot.loiteBackend.util.file.FileService;
import com.boot.loiteBackend.util.file.FileUploadResult;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SupportResourceServiceImpl implements SupportResourceService {

    private final SupportResourceRepository resourceRepository;
    private final AdminProductRepository adminProductRepository;
    private final FileService fileService;
    private final FileStorageProperties fileProps;
    private final SupportResourceMapper supportResourceMapper;

    private final String uploadCategory = "etc";

    @Override
    @Transactional
    public SupportResourceDto createResource(SupportResourceRequestDto dto, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new CustomException(ResourceErrorCode.INVALID_FILE);
        }

        FileUploadResult uploadResult;
        try {
            uploadResult = fileService.save(file, uploadCategory);
        } catch (Exception e) {
            throw new CustomException(ResourceErrorCode.FILE_UPLOAD_FAILED);
        }

        try {
            AdminProductEntity product = adminProductRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new CustomException(ResourceErrorCode.INVALID_PRODUCT));

            SupportResourceEntity entity = SupportResourceEntity.builder()
                    .product(product)
                    .displayYn(dto.getDisplayYn())
                    .resourceFileName(file.getOriginalFilename())
                    .resourceFileUrl(uploadResult.getUrlPath())
                    .resourceFilePath(uploadResult.getPhysicalPath())
                    .resourceFileSize(file.getSize())
                    .resourceFileType(file.getContentType())
                    .build();

            resourceRepository.save(entity);
            return supportResourceMapper.toDto(entity);
        } catch (Exception e) {
            throw new CustomException(ResourceErrorCode.SAVE_FAILED);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SupportResourceDto> getPagedResources(String keyword, Pageable pageable) {
        Page<SupportResourceEntity> page;
        if (StringUtils.hasText(keyword)) {
            page = resourceRepository.findByKeyword(keyword, pageable);
        } else {
            page = resourceRepository.findAll(pageable);
        }
        return page.map(supportResourceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public SupportResourceDto getResourceById(Long id) {
        SupportResourceEntity entity = resourceRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResourceErrorCode.NOT_FOUND));
        return supportResourceMapper.toDto(entity);
    }

    @Override
    @Transactional
    public SupportResourceDto updateResource(Long id, SupportResourceRequestDto request, MultipartFile file) {
        SupportResourceEntity entity = resourceRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResourceErrorCode.NOT_FOUND));

        AdminProductEntity product = adminProductRepository.findById(request.getProductId())
                .orElseThrow(() -> new CustomException(ResourceErrorCode.INVALID_PRODUCT));

        entity.setProduct(product);
        entity.setDisplayYn(request.getDisplayYn());

        if (file != null && !file.isEmpty()) {
            if (entity.getResourceFilePath() != null && !entity.getResourceFilePath().isBlank()) {
                deletePhysicalFile(entity.getResourceFilePath());
            }
            try {
                FileUploadResult uploadResult = fileService.save(file, uploadCategory);
                entity.setResourceFileName(file.getOriginalFilename());
                entity.setResourceFileUrl(uploadResult.getUrlPath());
                entity.setResourceFilePath(uploadResult.getPhysicalPath());
                entity.setResourceFileSize(file.getSize());
                entity.setResourceFileType(file.getContentType());
            } catch (Exception e) {
                throw new CustomException(ResourceErrorCode.FILE_UPLOAD_FAILED);
            }
        }

        return supportResourceMapper.toDto(entity);
    }

    @Override
    @Transactional
    public void deleteResource(Long id) {
        SupportResourceEntity entity = resourceRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResourceErrorCode.NOT_FOUND));

        if (entity.getResourceFilePath() != null && !entity.getResourceFilePath().isBlank()) {
            deletePhysicalFile(entity.getResourceFilePath());
        }

        try {
            resourceRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomException(ResourceErrorCode.DELETE_FAILED);
        }
    }

    private void deletePhysicalFile(String physicalPath) {
        File targetFile = new File(physicalPath);
        if (targetFile.exists() && !targetFile.delete()) {
            throw new CustomException(ResourceErrorCode.FILE_DELETE_FAILED);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Resource> fileDownload(Long id) {
        SupportResourceEntity entity = resourceRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResourceErrorCode.NOT_FOUND));

        try {
            Path filePath = Paths.get(entity.getResourceFilePath()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                throw new CustomException(ResourceErrorCode.NOT_FOUND);
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

    @Override
    @Transactional(readOnly = true)
    public List<AdminProductSummaryDto> getProductsList() {
        List<AdminProductEntity> products = adminProductRepository.findProductsWithoutResource();

        return products.stream()
                .map(p -> AdminProductSummaryDto.builder()
                        .productId(p.getProductId())
                        .productName(p.getProductName())
                        .productModelName(p.getProductModelName())
                        .build())
                .collect(Collectors.toList());
    }
}
