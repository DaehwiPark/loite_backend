package com.boot.loiteBackend.admin.mainpage.popup.service;

import com.boot.loiteBackend.admin.mainpage.popup.dto.*;
import com.boot.loiteBackend.admin.mainpage.popup.repository.AdminMainpagePopupRepository;
import com.boot.loiteBackend.common.file.FileServiceImpl;
import com.boot.loiteBackend.domain.mainpage.popup.MainpagePopupEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminMainpagePopupServiceImpl implements AdminMainpagePopupService{

    private final AdminMainpagePopupRepository adminMainpagePopupRepository;
    private final FileServiceImpl fileService;

    @Override
    public List<AdminMainpagePopupDetailDto> listVisible(LocalDateTime now) {
        LocalDateTime ts = (now != null) ? now : LocalDateTime.now();
        return adminMainpagePopupRepository.findVisible(ts).stream()
                .map(this::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void update(Long popupId, AdminMainpagePopupUpdateDto req) {
        MainpagePopupEntity e = getOrThrow(popupId);

        // 문자열은 "빈문자열"이면 null로 저장하고 싶다면 normalize 사용
        if (req.getPopupTitle()   != null) e.setPopupTitle(normalize(req.getPopupTitle()));
        if (req.getPopupDetail()  != null) e.setPopupDetail(normalize(req.getPopupDetail()));
        if (req.getPopupImageUrl()!= null) e.setPopupImageUrl(normalize(req.getPopupImageUrl()));
        if (req.getPopupLinkUrl() != null) e.setPopupLinkUrl(normalize(req.getPopupLinkUrl()));

        if (req.getPopupTarget()   != null) e.setPopupTarget(req.getPopupTarget());
        if (req.getPopupIsActive() != null) e.setPopupIsActive(req.getPopupIsActive());
        if (req.getPopupSortOrder()!= null) e.setPopupSortOrder(req.getPopupSortOrder());

        if (req.getPopupStartAt()  != null) e.setPopupStartAt(req.getPopupStartAt());
        if (req.getPopupEndAt()    != null) e.setPopupEndAt(req.getPopupEndAt());

        // 기간 유효성
        checkSchedule(e.getPopupStartAt(), e.getPopupEndAt());
    }

    //공백이 돌아올 시 null로 문자열 변경
    private String normalize(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    @Override
    @Transactional
    public void setActive(Long popupId, boolean active) {
        MainpagePopupEntity e = getOrThrow(popupId);
        e.setPopupIsActive(active);
    }

    @Override
    @Transactional
    public void bulkSetActive(List<Long> ids, boolean active) {
        if (ids == null || ids.isEmpty()) return;
        // 커스텀 쿼리 사용 (성능 좋음). 없다면 아래 루프 대체 가능.
        adminMainpagePopupRepository.bulkUpdateActive(ids, active);
    }

    @Override
    @Transactional
    public void reorder(List<Long> orderedIds) {
        if (orderedIds == null || orderedIds.isEmpty()) return;

        int order = 10;
        for (Long id : orderedIds) {
            int cnt = adminMainpagePopupRepository.updateSortOrder(id, order);
            if (cnt == 0) {
                // 존재하지 않는 id 무시 또는 예외; 여기선 로깅만
                log.warn("reorder() skipped unknown id={}", id);
            }
            order += 10;
        }
    }

    @Override
    @Transactional
    public void softDelete(Long popupId) {
        MainpagePopupEntity e = getOrThrow(popupId);
        e.setPopupIsActive(false);
        e.setPopupDeletedAt(LocalDateTime.now());
    }

    // ---------- 내부 유틸 ----------

    private MainpagePopupEntity getOrThrow(Long id) {
        return adminMainpagePopupRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Popup not found: id=" + id) // 필요 시 커스텀 예외로 교체
        );
    }

    private void checkSchedule(LocalDateTime start, LocalDateTime end) {
        if (start != null && end != null && end.isBefore(start)) {
            throw new IllegalArgumentException("popup_end_at must be >= popup_start_at");
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private AdminMainpagePopupDetailDto toItemDto(MainpagePopupEntity e) {
        return AdminMainpagePopupDetailDto.builder()
                .popupId(e.getPopupId())
                .popupTitle(e.getPopupTitle())
                .popupDetail(e.getPopupDetail())
                .popupImageUrl(e.getPopupImageUrl())
                .popupLinkUrl(e.getPopupLinkUrl())
                .popupTarget(e.getPopupTarget())
                .popupIsActive(e.isPopupIsActive())
                .popupSortOrder(e.getPopupSortOrder())
                .popupStartAt(e.getPopupStartAt())
                .popupEndAt(e.getPopupEndAt())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .popupDeletedAt(e.getPopupDeletedAt())
                .build();
    }

    @Override
    @Transactional
    public Long create(AdminMainpagePopupCreateDto req) {
        if (req == null) throw new IllegalArgumentException("CreateReq is null");
        if (req.getPopupLinkUrl() == null || req.getPopupLinkUrl().trim().isEmpty())
            throw new IllegalArgumentException("popup_link_url is required");

        // 기간 검증 재사용
        checkSchedule(req.getPopupStartAt(), req.getPopupEndAt());

        int nextOrder = adminMainpagePopupRepository.findMaxSortOrderOfActive() + 10;

        MainpagePopupEntity e = MainpagePopupEntity.builder()
                // A안: null 저장 (DB/엔티티가 NULL 허용이어야 함)
                .popupImageUrl(null)
                // B안(대안): .popupImageUrl(DEFAULT_PLACEHOLDER_IMG)
                .popupTitle(req.getPopupTitle())
                .popupDetail(req.getPopupDetail())
                .popupLinkUrl(req.getPopupLinkUrl())
                .popupTarget(req.getPopupTarget() != null ? req.getPopupTarget() : MainpagePopupEntity.Target._self)
                .popupIsActive(req.isPopupIsActive())
                .popupSortOrder(nextOrder)
                .popupStartAt(req.getPopupStartAt())
                .popupEndAt(req.getPopupEndAt())
                .build();

        adminMainpagePopupRepository.save(e);
        return e.getPopupId();
    }

    @Override
    @Transactional
    public Long create(AdminMainpagePopupCreateDto req, MultipartFile image) throws IOException {
        if (req == null) throw new IllegalArgumentException("CreateReq is null");
        if (req.getPopupLinkUrl() == null || req.getPopupLinkUrl().trim().isEmpty())
            throw new IllegalArgumentException("popup_link_url is required");

        // 기간 검증
        checkSchedule(req.getPopupStartAt(), req.getPopupEndAt());

        int nextOrder = adminMainpagePopupRepository.findMaxSortOrderOfActive() + 10;

        // 1) 이미지 파일 저장 (/uploads/popup)
        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            var result = fileService.save(image, "popup"); // ← 폴더: /uploads/popup/*
            imageUrl = result.getUrlPath();               // 예) /uploads/popup/uuid_name.png
        }

        // 2) 엔티티 저장 (URL 세팅)
        MainpagePopupEntity e = MainpagePopupEntity.builder()
                .popupImageUrl(imageUrl)  // ★ 파일이 있으면 /uploads/popup/*, 없으면 null
                .popupTitle(req.getPopupTitle())
                .popupDetail(req.getPopupDetail())
                .popupLinkUrl(req.getPopupLinkUrl())
                .popupTarget(req.getPopupTarget() != null ? req.getPopupTarget() : MainpagePopupEntity.Target._self)
                .popupIsActive(req.isPopupIsActive())
                .popupSortOrder(nextOrder)
                .popupStartAt(req.getPopupStartAt())
                .popupEndAt(req.getPopupEndAt())
                .build();

        adminMainpagePopupRepository.save(e);
        return e.getPopupId();
    }


    @Override
    public AdminMainpagePopupDetailDto getOne(Long id) {
        return toItemDto(getOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<AdminMainpagePopupListItemDto> listAllForAdminSummary(Integer titleMax, Integer detailMax) {
        int tMax = titleMax == null ? 0 : Math.max(0, titleMax);
        int dMax = detailMax == null ? 0 : Math.max(0, detailMax);

        return adminMainpagePopupRepository.findAllForAdminOrder().stream()
                .map(p -> new AdminMainpagePopupListItemDto(
                        p.getPopupId(),
                        summarize(p.getPopupTitle(), tMax),
                        summarize(p.getPopupDetail(), dMax),
                        p.getPopupImageUrl(),
                        p.getPopupLinkUrl(),
                        p.getPopupTarget(),
                        p.isPopupIsActive(),
                        p.getPopupSortOrder(),
                        p.getPopupStartAt(),
                        p.getPopupEndAt(),
                        p.getCreatedAt()
                ))
                .toList();
    }

    private static String summarize(String s, int max) {
        if (s == null) return "";
        if (max <= 0 || s.length() <= max) return s;
        return s.substring(0, max) + "…";  // U+2026
    }
}
