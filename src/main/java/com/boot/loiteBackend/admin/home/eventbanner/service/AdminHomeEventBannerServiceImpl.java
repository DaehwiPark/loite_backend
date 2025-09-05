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
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

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
    public AdminHomeEventBannerResponseDto create(AdminHomeEventBannerCreateRequestDto req, MultipartFile pcImage,  MultipartFile mobileImage, Long loginUserId) {
        if (loginUserId == null)
            throw new CustomException(UNAUTHORIZED);
        if (req == null)
            throw new CustomException(INVALID_REQUEST);

        try {
            HomeEventBannerEntity e = mapper.toEntity(req);

            // 기본값/정규화
            e.setDisplayYn(YnUtils.normalizeYnOrDefault(e.getDisplayYn(), "Y"));
            e.setLinkTarget(TextUtils.isBlank(e.getLinkTarget()) ? "_self" : e.getLinkTarget());
            if (e.getSortOrder() == null) e.setSortOrder(0);

            // 감사필드
            e.setCreatedBy(loginUserId);
            e.setUpdatedBy(loginUserId);
            // createdAt/updatedAt 은 DB 기본값 사용 시 세팅 불필요

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
        } catch (Exception e) {
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

        // Y/N 정규화 (명시 없으면 유지)
        e.setDisplayYn(YnUtils.normalizeYnOrDefault(e.getDisplayYn(), "Y"));

        // 감사필드
        e.setUpdatedBy(loginUserId);

        try {
            HomeEventBannerEntity saved = repository.save(e);

            // ===== 캡처용 final 복사본 생성 (중요) =====
            final boolean pcReplacedFinal = pcReplaced;
            final boolean moReplacedFinal = moReplaced;
            final String newPcPathFinal = newPcPath;
            final String newMoPathFinal = newMoPath;

            // 커밋 후 기존 파일 삭제(성공 시에만)
            if (pcReplacedFinal || moReplacedFinal) {
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        try {
                            if (pcReplacedFinal && oldPcPath != null && !oldPcPath.equals(newPcPathFinal)) {
                                fileService.deleteQuietly(oldPcPath);
                            }
                        } catch (Exception ignore) {}

                        try {
                            if (moReplacedFinal && oldMoPath != null && !oldMoPath.equals(newMoPathFinal)) {
                                fileService.deleteQuietly(oldMoPath);
                            }
                        } catch (Exception ignore) {}
                    }
                });
            }

            return mapper.toAdminResponse(saved);

        } catch (DataAccessException dae) {
            // DB 실패 시 새로 업로드한 파일 보상 삭제
            try {
                if (pcReplaced && newPcPath != null) fileService.deleteQuietly(newPcPath);
            } catch (Exception ignore) {}
            try {
                if (moReplaced && newMoPath != null) fileService.deleteQuietly(newMoPath);
            } catch (Exception ignore) {}
            throw new CustomException(SAVE_FAILED);
        }
    }

    public void delete(Long id, Long loginUserId) {
        if (loginUserId == null) throw new CustomException(UNAUTHORIZED);
        if (id == null) throw new CustomException(INVALID_REQUEST);

        HomeEventBannerEntity e = repository.findById(id)
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        // 파일 경로 백업
        final String pcPath = e.getPcImagePath();
        final String moPath = e.getMobileImagePath();

        repository.delete(e); // 물리 삭제

        // 커밋 후 파일 삭제
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

        // 기간 유효성 체크 (startFrom <= endTo)
        if (filter != null && filter.getStartFrom() != null && filter.getEndTo() != null) {
            if (filter.getStartFrom().isAfter(filter.getEndTo())) {
                throw new CustomException(INVALID_REQUEST);
            }
        }

        Pageable safe = PageUtils.safePageable(pageable, defaultSort());

        Specification<HomeEventBannerEntity> spec =
                AdminHomeEventBannerSpecification.buildListSpec(filter);
        Page<HomeEventBannerEntity> page = repository.findAll(spec, safe);
        return page.map(mapper::toAdminResponse);
    }
}
