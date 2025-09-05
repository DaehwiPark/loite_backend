package com.boot.loiteBackend.admin.home.hero.service;

import com.boot.loiteBackend.admin.home.hero.dto.*;
import com.boot.loiteBackend.admin.home.hero.error.AdminHomeHeroErrorCode;
import com.boot.loiteBackend.admin.home.hero.repository.AdminHomeHeroRepository;
import com.boot.loiteBackend.admin.home.hero.spec.AdminHomeHeroSpecification;
import com.boot.loiteBackend.common.file.FileService;
import com.boot.loiteBackend.common.file.FileUploadResult;
import com.boot.loiteBackend.common.util.PageUtils;
import com.boot.loiteBackend.common.util.TextUtils;
import com.boot.loiteBackend.common.util.YnUtils;
import com.boot.loiteBackend.domain.home.hero.entity.HomeHeroEntity;
import com.boot.loiteBackend.domain.home.hero.mapper.HomeHeroMapper;
import com.boot.loiteBackend.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminHomeHeroServiceImpl implements AdminHomeHeroService {

    private final AdminHomeHeroRepository repository;
    private final HomeHeroMapper mapper;
    private final FileService fileService;

    private static final String UPLOAD_CATEGORY = "etc";

    /**
     * 기본 정렬: sortOrder ASC → startAt DESC → id DESC
     */
    private Sort defaultSort() {
        return Sort.by(
                Sort.Order.asc("sortOrder"),
                Sort.Order.desc("startAt"),
                Sort.Order.desc("id")
        );
    }

    @Override
    public AdminHomeHeroResponseDto create(AdminHomeHeroCreateRequestDto req, MultipartFile image, Long loginUserId) {
        if (loginUserId == null) throw new CustomException(AdminHomeHeroErrorCode.UNAUTHORIZED);
        if (req == null) throw new CustomException(AdminHomeHeroErrorCode.INVALID_REQUEST);
        if (image == null || image.isEmpty()) throw new CustomException(AdminHomeHeroErrorCode.INVALID_FILE);

        final FileUploadResult uploadResult;
        try {
            uploadResult = fileService.save(image, UPLOAD_CATEGORY);
        } catch (Exception e) {
            throw new CustomException(AdminHomeHeroErrorCode.FILE_UPLOAD_FAILED);
        }

        try {
            HomeHeroEntity entity = mapper.toEntity(req);

            // 파일 메타 (DDL: HOME_HERO_LEFT_IMAGE_*)
            entity.setLeftImageName(image.getOriginalFilename());
            entity.setLeftImageType(image.getContentType());
            entity.setLeftImageSize(image.getSize());
            entity.setLeftImageUrl(uploadResult.getUrlPath());
            entity.setLeftImagePath(uploadResult.getPhysicalPath());

            // 이미지 URL이 비어있다면 업로드 URL로 보정(보통은 위에서 세팅되므로 필요 없음)
            if (TextUtils.isBlank(entity.getLeftImageUrl())) {
                entity.setLeftImageUrl(uploadResult.getUrlPath());
            }

            // 기본값/정규화
            if (entity.getSortOrder() == null) entity.setSortOrder(1);
            entity.setDisplayYn(YnUtils.normalizeYnOrDefault(entity.getDisplayYn(), "Y"));
            entity.setDeletedYn(YnUtils.normalizeYnOrDefault(entity.getDeletedYn(), "N"));

            // 감사필드 (createdAt/updatedAt은 DB 기본값/트리거로 관리)
            entity.setCreatedBy(loginUserId);
            entity.setUpdatedBy(loginUserId);

            HomeHeroEntity saved = repository.save(entity);
            return mapper.toAdminResponse(saved);

        } catch (DataAccessException dae) {
            throw new CustomException(AdminHomeHeroErrorCode.SAVE_FAILED);
        } catch (CustomException ce) {
            throw ce;
        } catch (Exception e) {
            throw new CustomException(AdminHomeHeroErrorCode.SAVE_FAILED);
        }
    }

    @Override
    public AdminHomeHeroResponseDto update(AdminHomeHeroUpdateRequestDto req, MultipartFile image, Long loginUserId) {
        if (loginUserId == null) throw new CustomException(AdminHomeHeroErrorCode.UNAUTHORIZED);
        if (req == null || req.getId() == null) throw new CustomException(AdminHomeHeroErrorCode.INVALID_REQUEST);

        HomeHeroEntity entity = repository.findById(req.getId())
                .orElseThrow(() -> new CustomException(AdminHomeHeroErrorCode.NOT_FOUND));

        if ("Y".equalsIgnoreCase(entity.getDeletedYn())) {
            throw new CustomException(AdminHomeHeroErrorCode.ALREADY_DELETED);
        }

        // 변경 전 값 스냅샷
        final String oldPhysicalPath = entity.getLeftImagePath();
        final String oldUrl = entity.getLeftImageUrl();

        // 1) 다른 필드 patch
        mapper.updateEntityFromDto(req, entity);

        // 2) 파일 교체(멀티파트) 처리
        String newPhysicalPath = null;
        FileUploadResult uploadResult = null;

        if (image != null && !image.isEmpty()) {
            try {
                uploadResult = fileService.save(image, UPLOAD_CATEGORY);
                newPhysicalPath = uploadResult.getPhysicalPath();

                entity.setLeftImageName(image.getOriginalFilename());
                entity.setLeftImageType(image.getContentType());
                entity.setLeftImageSize(image.getSize());
                entity.setLeftImageUrl(uploadResult.getUrlPath());
                entity.setLeftImagePath(uploadResult.getPhysicalPath());
            } catch (Exception e) {
                throw new CustomException(AdminHomeHeroErrorCode.FILE_UPLOAD_FAILED);
            }
        } else {
            // 2-1) 멀티파트 없이 URL만 변경된 경우(예: 외부 CDN으로 교체)
            //      URL이 바뀌었고 기존에 물리파일이 있으면, 커밋 후 기존 파일 삭제 + 파일 메타 정리
            String newUrl = entity.getLeftImageUrl();
            boolean urlChanged = (newUrl != null && !newUrl.equals(oldUrl)) || (newUrl == null && oldUrl != null);

            if (urlChanged && oldPhysicalPath != null) {
                // 외부 URL 교체라고 가정: 물리 파일 메타는 정리(null) 권장
                entity.setLeftImageName(null);
                entity.setLeftImageType(null);
                entity.setLeftImageSize(null);
                entity.setLeftImagePath(null);

                // 커밋 후 삭제를 위해 afterCommit에 등록 (아래 try 블록에서 함께 등록)
            }
        }

        // 3) Y/N 정규화
        entity.setDisplayYn(YnUtils.normalizeYnOrDefault(entity.getDisplayYn(), "Y"));
        entity.setDeletedYn(YnUtils.normalizeYnOrDefault(entity.getDeletedYn(), "N"));

        // 4) 감사필드 (updatedAt은 DB가 관리)
        entity.setUpdatedBy(loginUserId);

        try {
            HomeHeroEntity saved = repository.save(entity);

            // 커밋 후 파일 정리 로직 등록
            final String oldPathFinal = oldPhysicalPath;
            final String newPathFinal = newPhysicalPath;
            final boolean needDeleteOld =
                    // 새 업로드가 있어 경로가 바뀐 경우
                    (uploadResult != null && oldPathFinal != null && !oldPathFinal.equals(newPathFinal))
                            ||
                            // 멀티파트 없이 URL만 바뀌었고, 기존 물리 파일이 있었던 경우
                            (uploadResult == null && oldPathFinal != null && !oldPathFinal.equals(entity.getLeftImagePath()));

            if (needDeleteOld) {
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        try {
                            fileService.deleteQuietly(oldPathFinal);
                        } catch (Exception ignore) {
                            /* 로그만 원하면 여기서 남겨도 됨 */
                        }
                    }
                });
            }

            return mapper.toAdminResponse(saved);

        } catch (DataAccessException dae) {
            // DB 저장 실패 시 새로 업로드한 파일 보상 삭제
            if (newPhysicalPath != null) {
                try {
                    fileService.deleteQuietly(newPhysicalPath);
                } catch (Exception ignore) {
                }
            }
            throw new CustomException(AdminHomeHeroErrorCode.SAVE_FAILED);
        }
    }

    @Override
    public void delete(Long id, Long loginUserId) {
        if (loginUserId == null) throw new CustomException(AdminHomeHeroErrorCode.UNAUTHORIZED);
        if (id == null) throw new CustomException(AdminHomeHeroErrorCode.INVALID_REQUEST);

        HomeHeroEntity entity = repository.findById(id)
                .orElseThrow(() -> new CustomException(AdminHomeHeroErrorCode.NOT_FOUND));

        if ("Y".equalsIgnoreCase(entity.getDeletedYn())) {
            throw new CustomException(AdminHomeHeroErrorCode.ALREADY_DELETED);
        }

        entity.setDeletedYn("Y");
        entity.setUpdatedBy(loginUserId);
        repository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminHomeHeroResponseDto detail(Long id) {
        if (id == null) throw new CustomException(AdminHomeHeroErrorCode.INVALID_REQUEST);
        HomeHeroEntity entity = repository.findById(id)
                .orElseThrow(() -> new CustomException(AdminHomeHeroErrorCode.NOT_FOUND));
        return mapper.toAdminResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdminHomeHeroResponseDto> list(Pageable pageable, AdminHomeHeroListRequestDto filter) {
        if (filter != null && filter.getStartFrom() != null && filter.getEndTo() != null) {
            if (filter.getStartFrom().isAfter(filter.getEndTo())) {
                throw new CustomException(AdminHomeHeroErrorCode.INVALID_REQUEST);
            }
        }

        Pageable safe = PageUtils.safePageable(pageable, defaultSort());
        Specification<HomeHeroEntity> spec = AdminHomeHeroSpecification.buildListSpec(filter);
        Page<HomeHeroEntity> page = repository.findAll(spec, safe);
        return page.map(mapper::toAdminResponse);
    }
}
