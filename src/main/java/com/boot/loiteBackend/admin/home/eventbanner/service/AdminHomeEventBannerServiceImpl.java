package com.boot.loiteBackend.admin.home.eventbanner.service;

import com.boot.loiteBackend.admin.home.eventbanner.dto.*;
import com.boot.loiteBackend.admin.home.eventbanner.repository.AdminHomeEventBannerRepository;
import com.boot.loiteBackend.admin.home.eventbanner.spec.AdminHomeEventBannerSpecification;
import com.boot.loiteBackend.common.util.PageUtils;
import com.boot.loiteBackend.common.util.TextUtils;
import com.boot.loiteBackend.common.util.YnUtils;
import com.boot.loiteBackend.common.file.FileService;
import com.boot.loiteBackend.common.file.FileUploadResult;
import com.boot.loiteBackend.domain.home.eventbanner.entity.HomeEventBannerEntity;
import com.boot.loiteBackend.domain.home.eventbanner.mapper.HomeEventBannerMapper;
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

import static com.boot.loiteBackend.admin.home.eventbanner.error.AdminHomeEventBannerErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminHomeEventBannerServiceImpl implements AdminHomeEventBannerService {

    private final AdminHomeEventBannerRepository repository;
    private final HomeEventBannerMapper mapper;
    private final FileService fileService;

    private static final String UPLOAD_CATEGORY = "etc";

    /**
     * 기본 정렬: startAt DESC → id DESC (sortOrder 제거됨)
     */
    private Sort defaultSort() {
        return Sort.by(
                Sort.Order.desc("startAt"),
                Sort.Order.desc("id")
        );
    }

    /**
     * DEFAULT_YN='Y' 규칙 검증: DEFAULT_SLOT은 1 또는 2만 허용
     */
    private void validateDefaultRule(String defaultYn, Integer defaultSlot) {
        if ("Y".equalsIgnoreCase(defaultYn)) {
            if (defaultSlot == null || (defaultSlot != 1 && defaultSlot != 2)) {
                throw new CustomException(INVALID_REQUEST);
            }
        }
    }

    /**
     * DEFAULT_YN='Y' 저장 전에 같은 슬롯의 기존 기본값들을 'N'으로 초기화
     */
    private void ensureSingleDefaultPerSlot(String defaultYn, Integer defaultSlot, Long excludeId) {
        if ("Y".equalsIgnoreCase(defaultYn)) {
            repository.clearDefaultYnBySlot(defaultSlot, excludeId);
        }
    }

    @Override
    public AdminHomeEventBannerResponseDto create(AdminHomeEventBannerCreateRequestDto req,
                                                  MultipartFile pcImage,
                                                  MultipartFile mobileImage,
                                                  Long loginUserId) {
        if (loginUserId == null) throw new CustomException(UNAUTHORIZED);
        if (req == null) throw new CustomException(INVALID_REQUEST);

        try {
            HomeEventBannerEntity e = mapper.toEntity(req);

            // 기본값/정규화
            e.setDisplayYn(YnUtils.normalizeYnOrDefault(e.getDisplayYn(), "Y"));
            e.setDefaultYn(YnUtils.normalizeYnOrDefault(e.getDefaultYn(), "N"));
            e.setLinkTarget(TextUtils.isBlank(e.getLinkTarget()) ? "_self" : e.getLinkTarget());

            // DEFAULT 규칙 검증 및 단일성 보장 (등록은 excludeId=null)
            validateDefaultRule(e.getDefaultYn(), e.getDefaultSlot());
            ensureSingleDefaultPerSlot(e.getDefaultYn(), e.getDefaultSlot(), null);

            // 감사필드
            e.setCreatedBy(loginUserId);
            e.setUpdatedBy(loginUserId);

            // 파일 업로드 (PC)
            if (pcImage != null && !pcImage.isEmpty()) {
                FileUploadResult r = fileService.save(pcImage, UPLOAD_CATEGORY);
                e.setPcImageName(pcImage.getOriginalFilename());
                e.setPcImageType(pcImage.getContentType());
                e.setPcImageSize(pcImage.getSize());
                e.setPcImageUrl(r.getUrlPath());
                e.setPcImagePath(r.getPhysicalPath());
            }
            // 파일 업로드 (Mobile)
            if (mobileImage != null && !mobileImage.isEmpty()) {
                FileUploadResult r = fileService.save(mobileImage, UPLOAD_CATEGORY);
                e.setMobileImageName(mobileImage.getOriginalFilename());
                e.setMobileImageType(mobileImage.getContentType());
                e.setMobileImageSize(mobileImage.getSize());
                e.setMobileImageUrl(r.getUrlPath());
                e.setMobileImagePath(r.getPhysicalPath());
            }

            HomeEventBannerEntity saved = repository.save(e);
            return mapper.toAdminResponse(saved);

        } catch (DataAccessException dae) {
            throw new CustomException(SAVE_FAILED);
        } catch (CustomException ce) {
            throw ce;
        } catch (Exception ex) {
            throw new CustomException(SAVE_FAILED);
        }
    }

    @Override
    public AdminHomeEventBannerResponseDto update(AdminHomeEventBannerUpdateRequestDto req,
                                                  MultipartFile pcImage,
                                                  MultipartFile mobileImage,
                                                  Long loginUserId) {
        if (loginUserId == null) throw new CustomException(UNAUTHORIZED);
        if (req == null || req.getId() == null) throw new CustomException(INVALID_REQUEST);

        HomeEventBannerEntity e = repository.findById(req.getId())
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        // patch (null 무시)
        mapper.updateEntityFromDto(req, e);

        // Y/N 정규화 (명시 없으면 기존 값 유지)
        e.setDisplayYn(YnUtils.normalizeYnOrDefault(e.getDisplayYn(), e.getDisplayYn()));
        e.setDefaultYn(YnUtils.normalizeYnOrDefault(e.getDefaultYn(), e.getDefaultYn()));

        // DEFAULT 규칙 검증 및 단일성 보장 (자기 자신 제외)
        validateDefaultRule(e.getDefaultYn(), e.getDefaultSlot());
        ensureSingleDefaultPerSlot(e.getDefaultYn(), e.getDefaultSlot(), e.getId());

        // 교체 전 경로 백업
        final String oldPcPath = e.getPcImagePath();
        final String oldMoPath = e.getMobileImagePath();

        String newPcPath = null;
        String newMoPath = null;
        boolean pcReplaced = false;
        boolean moReplaced = false;

        // PC 이미지 교체
        if (pcImage != null && !pcImage.isEmpty()) {
            try {
                FileUploadResult r = fileService.save(pcImage, UPLOAD_CATEGORY);
                newPcPath = r.getPhysicalPath();
                e.setPcImageName(pcImage.getOriginalFilename());
                e.setPcImageType(pcImage.getContentType());
                e.setPcImageSize(pcImage.getSize());
                e.setPcImageUrl(r.getUrlPath());
                e.setPcImagePath(r.getPhysicalPath());
                pcReplaced = true;
            } catch (Exception ex) {
                throw new CustomException(FILE_UPLOAD_FAILED);
            }
        }

        // Mobile 이미지 교체
        if (mobileImage != null && !mobileImage.isEmpty()) {
            try {
                FileUploadResult r = fileService.save(mobileImage, UPLOAD_CATEGORY);
                newMoPath = r.getPhysicalPath();
                e.setMobileImageName(mobileImage.getOriginalFilename());
                e.setMobileImageType(mobileImage.getContentType());
                e.setMobileImageSize(mobileImage.getSize());
                e.setMobileImageUrl(r.getUrlPath());
                e.setMobileImagePath(r.getPhysicalPath());
                moReplaced = true;
            } catch (Exception ex) {
                throw new CustomException(FILE_UPLOAD_FAILED);
            }
        }

        // 감사필드
        e.setUpdatedBy(loginUserId);

        try {
            HomeEventBannerEntity saved = repository.save(e);

            // 커밋 후 기존 파일 삭제(성공 시에만)
            final boolean pcReplacedFinal = pcReplaced;
            final boolean moReplacedFinal = moReplaced;
            final String newPcPathFinal = newPcPath;
            final String newMoPathFinal = newMoPath;

            if (pcReplacedFinal || moReplacedFinal) {
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        try {
                            if (pcReplacedFinal && oldPcPath != null && !oldPcPath.equals(newPcPathFinal))
                                fileService.deleteQuietly(oldPcPath);
                        } catch (Exception ignore) {
                        }
                        try {
                            if (moReplacedFinal && oldMoPath != null && !oldMoPath.equals(newMoPathFinal))
                                fileService.deleteQuietly(oldMoPath);
                        } catch (Exception ignore) {
                        }
                    }
                });
            }

            return mapper.toAdminResponse(saved);

        } catch (DataAccessException dae) {
            try {
                if (pcReplaced && newPcPath != null) fileService.deleteQuietly(newPcPath);
            } catch (Exception ignore) {
            }
            try {
                if (moReplaced && newMoPath != null) fileService.deleteQuietly(newMoPath);
            } catch (Exception ignore) {
            }
            throw new CustomException(SAVE_FAILED);
        }
    }

    @Override
    public void delete(Long id, Long loginUserId) {
        if (loginUserId == null) throw new CustomException(UNAUTHORIZED);
        if (id == null) throw new CustomException(INVALID_REQUEST);

        HomeEventBannerEntity e = repository.findById(id)
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        final String pcPath = e.getPcImagePath();
        final String moPath = e.getMobileImagePath();

        repository.delete(e); // 물리 삭제

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                try {
                    if (pcPath != null) fileService.deleteQuietly(pcPath);
                } catch (Exception ignore) {
                }
                try {
                    if (moPath != null) fileService.deleteQuietly(moPath);
                } catch (Exception ignore) {
                }
            }
        });
    }

    @Override
    @Transactional(readOnly = true)
    public AdminHomeEventBannerResponseDto detail(Long id) {
        if (id == null) throw new CustomException(INVALID_REQUEST);
        HomeEventBannerEntity e = repository.findById(id)
                .orElseThrow(() -> new CustomException(NOT_FOUND));
        return mapper.toAdminResponse(e);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdminHomeEventBannerResponseDto> list(Pageable pageable, AdminHomeEventBannerListRequestDto filter) {
        if (filter != null && filter.getStartFrom() != null && filter.getEndTo() != null) {
            if (filter.getStartFrom().isAfter(filter.getEndTo())) {
                throw new CustomException(INVALID_REQUEST);
            }
        }
        Pageable safe = PageUtils.safePageable(pageable, defaultSort());
        Specification<HomeEventBannerEntity> spec = AdminHomeEventBannerSpecification.buildListSpec(filter);
        Page<HomeEventBannerEntity> page = repository.findAll(spec, safe);
        return page.map(mapper::toAdminResponse);
    }
}