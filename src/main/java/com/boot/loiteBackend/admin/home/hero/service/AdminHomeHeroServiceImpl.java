package com.boot.loiteBackend.admin.home.hero.service;

import com.boot.loiteBackend.admin.home.hero.dto.AdminHomeHeroCreateRequestDto;
import com.boot.loiteBackend.admin.home.hero.dto.AdminHomeHeroDeleteRequestDto;
import com.boot.loiteBackend.admin.home.hero.dto.AdminHomeHeroListRequestDto;
import com.boot.loiteBackend.admin.home.hero.dto.AdminHomeHeroResponseDto;
import com.boot.loiteBackend.admin.home.hero.dto.AdminHomeHeroUpdateRequestDto;
import com.boot.loiteBackend.admin.home.hero.enums.PublishStatus;
import com.boot.loiteBackend.admin.home.hero.error.AdminHomeHeroErrorCode;
import com.boot.loiteBackend.admin.home.hero.repository.AdminHomeHeroRepository;
import com.boot.loiteBackend.common.file.FileService;
import com.boot.loiteBackend.common.file.FileUploadResult;
import com.boot.loiteBackend.domain.home.hero.entity.HomeHeroEntity;
import com.boot.loiteBackend.domain.home.hero.mapper.HomeHeroMapper;
import com.boot.loiteBackend.global.error.exception.CustomException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminHomeHeroServiceImpl implements AdminHomeHeroService {

    private final AdminHomeHeroRepository repository;
    private final HomeHeroMapper mapper;
    private final FileService fileService;

    // 파일 업로드 카테고리(스토리지 경로 정책에 맞게 수정)
    private final String uploadCategory = "etc";

    @Override
    @Transactional
    public AdminHomeHeroResponseDto create(AdminHomeHeroCreateRequestDto req, MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new CustomException(AdminHomeHeroErrorCode.INVALID_FILE);
        }

        FileUploadResult uploadResult;
        try {
            uploadResult = fileService.save(image, uploadCategory);
        } catch (Exception e) {
            throw new CustomException(AdminHomeHeroErrorCode.FILE_UPLOAD_FAILED);
        }

        try {
            HomeHeroEntity entity = mapper.toEntity(req);

            entity.setLeftResFileName(image.getOriginalFilename());
            entity.setLeftResFileType(image.getContentType());
            entity.setLeftResFileSize(image.getSize());
            entity.setLeftResFileUrl(uploadResult.getUrlPath());
            entity.setLeftResFilePath(uploadResult.getPhysicalPath());

            if (entity.getLeftImageUrl() == null || entity.getLeftImageUrl().isBlank()) {
                entity.setLeftImageUrl(uploadResult.getUrlPath());
            }

            if (entity.getSortOrder() == null) entity.setSortOrder(1);
            if (entity.getDisplayYn() == null || entity.getDisplayYn().isBlank()) entity.setDisplayYn("Y");
            if (entity.getPublishStatus() == null) entity.setPublishStatus(PublishStatus.DRAFT);


            HomeHeroEntity saved = repository.save(entity);

            return mapper.toAdminResponse(saved);

        } catch (Exception e) {
            throw new CustomException(AdminHomeHeroErrorCode.SAVE_FAILED);
        }
    }


    @Override
    @Transactional
    public AdminHomeHeroResponseDto update(AdminHomeHeroUpdateRequestDto req, MultipartFile image) {
        HomeHeroEntity entity = repository.findById(req.getId())
                .orElseThrow(() -> new IllegalArgumentException("HomeHero not found: " + req.getId()));

        if ("Y".equalsIgnoreCase(entity.getDeletedYn())) {
            throw new IllegalStateException("Already deleted content: " + req.getId());
        }

        // 1) DTO → Entity (부분 업데이트)
        mapper.updateEntityFromDto(req, entity);

        // 2) 이미지 교체 처리
        if (image != null && !image.isEmpty()) {
            FileUploadResult uploadResult;
            try {
                uploadResult = fileService.save(image, uploadCategory);
            } catch (Exception e) {
                throw new CustomException(AdminHomeHeroErrorCode.FILE_UPLOAD_FAILED);
            }

            entity.setLeftResFileName(image.getOriginalFilename());
            entity.setLeftResFileType(image.getContentType());
            entity.setLeftResFileSize(image.getSize());
            entity.setLeftResFileUrl(uploadResult.getUrlPath());
            entity.setLeftResFilePath(uploadResult.getPhysicalPath());

            // 좌측 이미지 URL도 교체
            entity.setLeftImageUrl(uploadResult.getUrlPath());
        }

        if (req.getUpdatedBy() != null) {
            entity.setUpdatedBy(req.getUpdatedBy());
        }
        entity.setUpdatedAt(LocalDateTime.now());

        HomeHeroEntity saved = repository.save(entity);
        return mapper.toAdminResponse(saved);
    }

    @Override
    public void delete(AdminHomeHeroDeleteRequestDto req) {
        HomeHeroEntity entity = repository.findById(req.getHomeHeroId())
                .orElseThrow(() -> new IllegalArgumentException("HomeHero not found: " + req.getHomeHeroId()));

        entity.setDeletedYn("Y");
        entity.setUpdatedBy(req.getUpdatedBy());
        entity.setUpdatedAt(LocalDateTime.now());

        // 스키마에 VERSION 컬럼이 없으므로 버전 증분 로직 제거
        repository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminHomeHeroResponseDto detail(Long id) {
        HomeHeroEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("HomeHero not found: " + id));
        return mapper.toAdminResponse(entity); // ← 일관된 Admin 응답
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdminHomeHeroResponseDto> list(AdminHomeHeroListRequestDto filter, int page, int size) {
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.max(size, 1),
                Sort.by(
                        Sort.Order.asc("sortOrder"),
                        Sort.Order.desc("startAt"),
                        Sort.Order.desc("id")
                )
        );

        Specification<HomeHeroEntity> spec = buildSpec(filter);
        Page<HomeHeroEntity> result = repository.findAll(spec, pageable);

        // Page<Entity> → Page<Admin DTO>
        return mapper.toAdminResponsePage(result);
    }

    private Specification<HomeHeroEntity> buildSpec(AdminHomeHeroListRequestDto f) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 소프트 삭제 제외
            predicates.add(cb.equal(cb.upper(root.get("deletedYn")), "N"));

            if (f != null) {
                if (f.getPublishStatus() != null && !f.getPublishStatus().isBlank()) {
                    predicates.add(cb.equal(root.get("publishStatus"), f.getPublishStatus()));
                }
                if (f.getDisplayYn() != null && !f.getDisplayYn().isBlank()) {
                    predicates.add(cb.equal(cb.upper(root.get("displayYn")), f.getDisplayYn().toUpperCase()));
                }
                if (f.getStartFrom() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("startAt"), f.getStartFrom()));
                }
                if (f.getEndTo() != null) { // include end date (<=)
                    predicates.add(cb.lessThanOrEqualTo(root.get("endAt"), f.getEndTo()));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
