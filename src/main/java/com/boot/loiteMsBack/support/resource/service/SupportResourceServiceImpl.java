package com.boot.loiteMsBack.support.resource.service;

import com.boot.loiteMsBack.config.FileStorageProperties;
import com.boot.loiteMsBack.global.error.exception.CustomException;
import com.boot.loiteMsBack.support.resource.dto.SupportResourceDto;
import com.boot.loiteMsBack.support.resource.dto.SupportResourceRequestDto;
import com.boot.loiteMsBack.support.resource.entity.SupportResourceEntity;
import com.boot.loiteMsBack.support.resource.error.ResourceErrorCode;
import com.boot.loiteMsBack.support.resource.mapper.SupportResourceMapper;
import com.boot.loiteMsBack.support.resource.repository.SupportResourceRepository;
import com.boot.loiteMsBack.util.file.FileService;
import com.boot.loiteMsBack.util.file.FileUploadResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SupportResourceServiceImpl implements SupportResourceService {

    private final SupportResourceRepository resourceRepository;
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
            SupportResourceEntity entity = SupportResourceEntity.builder()
                    .resourceProductName(dto.getResourceProductName())
                    .resourceModelName(dto.getResourceModelName())
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
    public List<SupportResourceDto> getAllResources() {
        return resourceRepository.findAll().stream()
                .map(supportResourceMapper::toDto)
                .collect(Collectors.toList());
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

        entity.setResourceProductName(request.getResourceProductName());
        entity.setResourceModelName(request.getResourceModelName());

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
}
