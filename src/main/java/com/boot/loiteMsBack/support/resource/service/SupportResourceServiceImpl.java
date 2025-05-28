package com.boot.loiteMsBack.support.resource.service;

import com.boot.loiteMsBack.config.FileStorageProperties;
import com.boot.loiteMsBack.support.resource.dto.SupportResourceDto;
import com.boot.loiteMsBack.support.resource.dto.SupportResourceRequestDto;
import com.boot.loiteMsBack.support.resource.entity.SupportResourceEntity;
import com.boot.loiteMsBack.support.resource.repository.SupportResourceRepository;
import com.boot.loiteMsBack.util.file.FileService;
import com.boot.loiteMsBack.util.file.FileUploadResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupportResourceServiceImpl implements SupportResourceService {

    private final SupportResourceRepository resourceRepository;
    private final FileService fileService;
    private final FileStorageProperties fileProps;
    private final String uploadCategory = "etc";

    public SupportResourceServiceImpl(SupportResourceRepository resourceRepository, FileService fileService, FileStorageProperties fileProps) {
        this.resourceRepository = resourceRepository;
        this.fileService = fileService;
        this.fileProps = fileProps;
    }

    @Override
    @Transactional
    public SupportResourceDto createResource(SupportResourceRequestDto dto, MultipartFile file) {
        // 1. 파일 저장 처리 (경로 반환)
        FileUploadResult uploadResult = fileService.save(file, uploadCategory);

        // 2. 파일 메타 정보 추출
        String fileName = file.getOriginalFilename();
        long fileSize = file.getSize();
        String fileType = file.getContentType();

        // 3. Entity 생성
        SupportResourceEntity entity = SupportResourceEntity.builder()
                .resourceProductName(dto.getResourceProductName())
                .resourceModelName(dto.getResourceModelName())
                .resourceFileName(fileName)
                .resourceFileUrl(uploadResult.getUrlPath())
                .resourceFilePath(uploadResult.getPhysicalPath())
                .resourceFileSize(fileSize)
                .resourceFileType(fileType)
                .build();

        // 4. 저장
        resourceRepository.save(entity);

        // 5. 응답 DTO 반환
        return new SupportResourceDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupportResourceDto> getAllResources() {
        return resourceRepository.findAll().stream()
                .map(SupportResourceDto::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SupportResourceDto getResourceById(Long id) {
        SupportResourceEntity entity = resourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found with id: " + id));
        return new SupportResourceDto(entity);
    }

    @Override
    @Transactional
    public SupportResourceDto updateResource(Long id, SupportResourceRequestDto request, MultipartFile file) {
        SupportResourceEntity entity = resourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found with id: " + id));

        // 1. 텍스트 정보 업데이트
        entity.setResourceProductName(request.getResourceProductName());
        entity.setResourceModelName(request.getResourceModelName());

        // 2. 파일이 새로 업로드된 경우
        if (file != null && !file.isEmpty()) {
            // 2-1. 기존 파일 삭제
            String oldFilePath = entity.getResourceFilePath();
            if (oldFilePath != null && !oldFilePath.isBlank()) {
                deletePhysicalFile(oldFilePath);
            }

            // 2-2. 새 파일 저장
            FileUploadResult uploadResult = fileService.save(file, uploadCategory);

            // 2-3. 엔티티 업데이트
            entity.setResourceFileName(file.getOriginalFilename());
            entity.setResourceFileUrl(uploadResult.getUrlPath());
            entity.setResourceFilePath(uploadResult.getPhysicalPath());
            entity.setResourceFileSize(file.getSize());
            entity.setResourceFileType(file.getContentType());
        }

        return new SupportResourceDto(entity);
    }

    @Override
    @Transactional
    public void deleteResource(Long id) {
        SupportResourceEntity entity = resourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found with id: " + id));

        // 파일 삭제
        if (entity.getResourceFilePath() != null && !entity.getResourceFilePath().isBlank()) {
            deletePhysicalFile(entity.getResourceFilePath());
        }

        resourceRepository.deleteById(id);
    }

    private void deletePhysicalFile(String physicalPath) {
        File targetFile = new File(physicalPath);
        if (targetFile.exists()) {
            boolean deleted = targetFile.delete();
            if (!deleted) {
                System.err.println("파일 삭제 실패: " + targetFile.getAbsolutePath());
            }
        }
    }
}
