package com.boot.loiteBackend.admin.support.resource.service;

import com.boot.loiteBackend.config.FileStorageProperties;
import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.admin.product.product.dto.ProductSummaryDto;
import com.boot.loiteBackend.admin.product.product.entity.ProductEntity;
import com.boot.loiteBackend.admin.product.product.repository.ProductRepository;
import com.boot.loiteBackend.admin.support.resource.dto.AdminSupportResourceDto;
import com.boot.loiteBackend.admin.support.resource.dto.AdminSupportResourceRequestDto;
import com.boot.loiteBackend.admin.support.resource.entity.AdminSupportResourceEntity;
import com.boot.loiteBackend.admin.support.resource.error.AdminResourceErrorCode;
import com.boot.loiteBackend.admin.support.resource.mapper.AdminSupportResourceMapper;
import com.boot.loiteBackend.admin.support.resource.repository.AdminSupportResourceRepository;
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
public class AdminSupportResourceServiceImpl implements AdminSupportResourceService {

    private final AdminSupportResourceRepository resourceRepository;
    private final ProductRepository productRepository;
    private final FileService fileService;
    private final FileStorageProperties fileProps;
    private final AdminSupportResourceMapper adminSupportResourceMapper;

    private final String uploadCategory = "etc";

    @Override
    @Transactional
    public AdminSupportResourceDto createResource(AdminSupportResourceRequestDto dto, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new CustomException(AdminResourceErrorCode.INVALID_FILE);
        }

        FileUploadResult uploadResult;
        try {
            uploadResult = fileService.save(file, uploadCategory);
        } catch (Exception e) {
            throw new CustomException(AdminResourceErrorCode.FILE_UPLOAD_FAILED);
        }

        try {
            ProductEntity product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new CustomException(AdminResourceErrorCode.INVALID_PRODUCT));

            AdminSupportResourceEntity entity = AdminSupportResourceEntity.builder()
                    .product(product)
                    .displayYn(dto.getDisplayYn())
                    .resourceFileName(file.getOriginalFilename())
                    .resourceFileUrl(uploadResult.getUrlPath())
                    .resourceFilePath(uploadResult.getPhysicalPath())
                    .resourceFileSize(file.getSize())
                    .resourceFileType(file.getContentType())
                    .build();

            resourceRepository.save(entity);
            return adminSupportResourceMapper.toDto(entity);
        } catch (Exception e) {
            throw new CustomException(AdminResourceErrorCode.SAVE_FAILED);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdminSupportResourceDto> getPagedResources(String keyword, Pageable pageable) {
        Page<AdminSupportResourceEntity> page;
        if (StringUtils.hasText(keyword)) {
            page = resourceRepository.findByKeyword(keyword, pageable);
        } else {
            page = resourceRepository.findAll(pageable);
        }
        return page.map(adminSupportResourceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminSupportResourceDto getResourceById(Long id) {
        AdminSupportResourceEntity entity = resourceRepository.findById(id)
                .orElseThrow(() -> new CustomException(AdminResourceErrorCode.NOT_FOUND));
        return adminSupportResourceMapper.toDto(entity);
    }

    @Override
    @Transactional
    public AdminSupportResourceDto updateResource(Long id, AdminSupportResourceRequestDto request, MultipartFile file) {
        AdminSupportResourceEntity entity = resourceRepository.findById(id)
                .orElseThrow(() -> new CustomException(AdminResourceErrorCode.NOT_FOUND));

        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new CustomException(AdminResourceErrorCode.INVALID_PRODUCT));

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
                throw new CustomException(AdminResourceErrorCode.FILE_UPLOAD_FAILED);
            }
        }

        return adminSupportResourceMapper.toDto(entity);
    }

    @Override
    @Transactional
    public void deleteResource(Long id) {
        AdminSupportResourceEntity entity = resourceRepository.findById(id)
                .orElseThrow(() -> new CustomException(AdminResourceErrorCode.NOT_FOUND));

        if (entity.getResourceFilePath() != null && !entity.getResourceFilePath().isBlank()) {
            deletePhysicalFile(entity.getResourceFilePath());
        }

        try {
            resourceRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomException(AdminResourceErrorCode.DELETE_FAILED);
        }
    }

    private void deletePhysicalFile(String physicalPath) {
        File targetFile = new File(physicalPath);
        if (targetFile.exists() && !targetFile.delete()) {
            throw new CustomException(AdminResourceErrorCode.FILE_DELETE_FAILED);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Resource> fileDownload(Long id) {
        AdminSupportResourceEntity entity = resourceRepository.findById(id)
                .orElseThrow(() -> new CustomException(AdminResourceErrorCode.NOT_FOUND));

        try {
            Path filePath = Paths.get(entity.getResourceFilePath()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                throw new CustomException(AdminResourceErrorCode.NOT_FOUND);
            }

            String encodedFileName = URLEncoder.encode(entity.getResourceFileName(), StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
                    .body(resource);

        } catch (MalformedURLException e) {
            throw new CustomException(AdminResourceErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductSummaryDto> getProductsList() {
        List<ProductEntity> products = productRepository.findProductsWithoutResource();

        return products.stream()
                .map(p -> ProductSummaryDto.builder()
                        .productId(p.getProductId())
                        .productName(p.getProductName())
                        .productModelName(p.getProductModelName())
                        .build())
                .collect(Collectors.toList());
    }
}
