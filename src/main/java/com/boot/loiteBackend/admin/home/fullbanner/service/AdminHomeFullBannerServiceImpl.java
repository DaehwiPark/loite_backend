package com.boot.loiteBackend.admin.home.fullbanner.service;

import com.boot.loiteBackend.admin.home.fullbanner.dto.*;
import com.boot.loiteBackend.admin.home.fullbanner.error.AdminHomeFullBannerErrorCode;
import com.boot.loiteBackend.admin.home.fullbanner.repository.AdminHomeFullBannerRepository;
import com.boot.loiteBackend.admin.home.fullbanner.spec.AdminHomeFullBannerSpecification;
import com.boot.loiteBackend.common.file.FileService;
import com.boot.loiteBackend.common.file.FileUploadResult;
import com.boot.loiteBackend.common.util.SortUtils;
import com.boot.loiteBackend.common.util.TextUtils;
import com.boot.loiteBackend.common.util.YnUtils;
import com.boot.loiteBackend.domain.home.fullbanner.entity.HomeFullBannerEntity;
import com.boot.loiteBackend.domain.home.fullbanner.mapper.HomeFullBannerMapper;
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

import java.util.Set;

import static com.boot.loiteBackend.admin.home.fullbanner.error.AdminHomeFullBannerErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminHomeFullBannerServiceImpl implements AdminHomeFullBannerService {

    private final AdminHomeFullBannerRepository repository;
    private final HomeFullBannerMapper mapper;
    private final FileService fileService;

    private static final String UPLOAD_CATEGORY = "etc";

    private static final Sort DEFAULT_SORT = Sort.by(
            Sort.Order.asc("sortOrder"),
            Sort.Order.desc("startAt"),
            Sort.Order.desc("id")
    );
    private static final Set<String> ALLOWED_SORTS = Set.of(
            "sortOrder", "startAt", "endAt", "id", "displayYn", "createdAt", "updatedAt"
    );

    @Override
    public AdminHomeFullBannerResponseDto create(AdminHomeFullBannerCreateRequestDto req,
                                                 MultipartFile bgImage,
                                                 Long loginUserId) {
        if (loginUserId == null) throw new CustomException(UNAUTHORIZED);
        if (req == null) throw new CustomException(INVALID_REQUEST);

        try {
            HomeFullBannerEntity e = mapper.toEntity(req);

            // 기본값/정규화
            e.setDisplayYn(YnUtils.normalizeYnOrDefault(e.getDisplayYn(), "Y"));
            e.setButtonLinkTarget(TextUtils.isBlank(e.getButtonLinkTarget()) ? "_self" : e.getButtonLinkTarget());
            if (e.getSortOrder() == null) e.setSortOrder(0);
            e.setDefaultYn(TextUtils.isBlank(e.getDefaultYn()) ? "N" : e.getDefaultYn());

            // 감사
            e.setCreatedBy(loginUserId);
            e.setUpdatedBy(loginUserId);

            // ★ 대표 단일성: 요청이 Y면 모두 N으로 내린 뒤 본 건을 Y로 저장
            final boolean wantsDefaultY = "Y".equalsIgnoreCase(e.getDefaultYn());
            if (wantsDefaultY) {
                repository.clearDefaultAll(loginUserId);
                e.setDefaultYn("Y"); // 명시적으로 유지
            } else {
                e.setDefaultYn("N");
            }

            // 배경 이미지 저장(선택)
            if (bgImage != null && !bgImage.isEmpty()) {
                FileUploadResult r = fileService.save(bgImage, UPLOAD_CATEGORY);
                e.setBgImageName(bgImage.getOriginalFilename());
                e.setBgImageType(bgImage.getContentType());
                e.setBgImageSize(bgImage.getSize());
                e.setBgImageUrl(r.getUrlPath());
                e.setBgImagePath(r.getPhysicalPath());
            }

            HomeFullBannerEntity saved = repository.save(e);
            return mapper.toAdminResponse(saved);

        } catch (DataAccessException dae) {
            throw new CustomException(SAVE_FAILED);
        } catch (CustomException ce) {
            throw ce;
        } catch (Exception e) {
            throw new CustomException(SAVE_FAILED);
        }
    }

    @Override
    public AdminHomeFullBannerResponseDto update(AdminHomeFullBannerUpdateRequestDto req,
                                                 MultipartFile bgImage,
                                                 Long loginUserId) {
        if (loginUserId == null) throw new CustomException(UNAUTHORIZED);
        if (req == null || req.getId() == null) throw new CustomException(INVALID_REQUEST);

        HomeFullBannerEntity e = repository.findById(req.getId())
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        // ★ 이번 요청이 defaultYn=Y 인지 미리 판단 후, 매퍼가 먼저 Y를 반영하지 못하게 차단
        final String keepDefaultFromDto = req.getDefaultYn();
        final boolean wantsDefaultY = keepDefaultFromDto != null && "Y".equalsIgnoreCase(keepDefaultFromDto);
        if (wantsDefaultY) {
            req.setDefaultYn(null);
        }

        // patch
        mapper.updateEntityFromDto(req, e);

        // 이미지 교체 준비
        final String oldBgPath = e.getBgImagePath();
        String newBgPath = null;
        boolean replaced = false;

        if (bgImage != null && !bgImage.isEmpty()) {
            try {
                FileUploadResult r = fileService.save(bgImage, UPLOAD_CATEGORY);
                newBgPath = r.getPhysicalPath();
                e.setBgImageName(bgImage.getOriginalFilename());
                e.setBgImageType(bgImage.getContentType());
                e.setBgImageSize(bgImage.getSize());
                e.setBgImageUrl(r.getUrlPath());
                e.setBgImagePath(r.getPhysicalPath());
                replaced = true;
            } catch (Exception ex) {
                throw new CustomException(AdminHomeFullBannerErrorCode.FILE_UPLOAD_FAILED);
            }
        }

        // Y/N 정규화
        e.setDisplayYn(YnUtils.normalizeYnOrDefault(e.getDisplayYn(), "Y"));

        // ★ 대표 단일성 반영
        if (wantsDefaultY) {
            // 본인 제외 전부 N으로 내린 다음 본 건만 Y
            repository.clearDefaultAllExcept(e.getId(), loginUserId);
            e.setDefaultYn("Y");
        } else if ("N".equalsIgnoreCase(keepDefaultFromDto)) {
            e.setDefaultYn("N");
        } else {
            // 명시 없으면 현재값 정규화
            e.setDefaultYn(TextUtils.isBlank(e.getDefaultYn()) ? "N" : e.getDefaultYn());
        }

        // 감사
        e.setUpdatedBy(loginUserId);

        try {
            HomeFullBannerEntity saved = repository.save(e);

            // 커밋 후 기존 파일 삭제(성공 시에만)
            if (replaced) {
                final String oldPathFinal = oldBgPath;
                final String newPathFinal = newBgPath;

                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        try {
                            if (oldPathFinal != null && !oldPathFinal.equals(newPathFinal)) {
                                fileService.deleteQuietly(oldPathFinal);
                            }
                        } catch (Exception ignore) {}
                    }
                });
            }

            // 원본 DTO 복구(선택)
            req.setDefaultYn(keepDefaultFromDto);

            return mapper.toAdminResponse(saved);

        } catch (DataAccessException dae) {
            if (replaced && newBgPath != null) {
                try { fileService.deleteQuietly(newBgPath); } catch (Exception ignore) {}
            }
            throw new CustomException(SAVE_FAILED);
        }
    }

    @Override
    public void delete(Long id, Long loginUserId) {
        if (loginUserId == null) throw new CustomException(UNAUTHORIZED);
        if (id == null) throw new CustomException(INVALID_REQUEST);

        HomeFullBannerEntity e = repository.findById(id)
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        final String bgPath = e.getBgImagePath();

        repository.delete(e);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                try {
                    if (bgPath != null) fileService.deleteQuietly(bgPath);
                } catch (Exception ignore) {
                }
            }
        });
    }

    @Override
    @Transactional(readOnly = true)
    public AdminHomeFullBannerResponseDto detail(Long id) {
        if (id == null) throw new CustomException(INVALID_REQUEST);
        HomeFullBannerEntity e = repository.findById(id)
                .orElseThrow(() -> new CustomException(NOT_FOUND));
        return mapper.toAdminResponse(e);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdminHomeFullBannerResponseDto> list(Pageable pageable, AdminHomeFullBannerListRequestDto filter) {
        if (filter != null && filter.getStartFrom() != null && filter.getEndTo() != null) {
            if (filter.getStartFrom().isAfter(filter.getEndTo())) {
                throw new CustomException(INVALID_REQUEST);
            }
        }

        Sort safeSort = SortUtils.whitelist(pageable.getSort(), ALLOWED_SORTS, DEFAULT_SORT);
        Pageable safe = PageRequest.of(
                Math.max(0, pageable.getPageNumber()),
                Math.max(1, pageable.getPageSize()),
                safeSort
        );

        Specification<HomeFullBannerEntity> spec = AdminHomeFullBannerSpecification.buildListSpec(filter);
        Page<HomeFullBannerEntity> page = repository.findAll(spec, safe);
        return page.map(mapper::toAdminResponse);
    }
}
