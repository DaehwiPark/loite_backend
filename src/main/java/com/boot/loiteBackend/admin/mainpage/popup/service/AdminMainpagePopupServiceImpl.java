package com.boot.loiteBackend.admin.mainpage.popup.service;

import com.boot.loiteBackend.admin.mainpage.popup.dto.AdminMainpagePopupDetailDto;
import com.boot.loiteBackend.admin.mainpage.popup.dto.AdminMainpagePopupDto;
import com.boot.loiteBackend.admin.mainpage.popup.dto.AdminMainpagePopupUpdateDto;
import com.boot.loiteBackend.admin.mainpage.popup.repository.AdminMainpagePopupRepository;
import com.boot.loiteBackend.domain.mainpage.popup.MainpagePopupEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminMainpagePopupServiceImpl implements AdminMainpagePopupService{

    private final AdminMainpagePopupRepository adminMainpagePopupRepository;

    // ---------- 조회 ----------

    @Override
    public List<AdminMainpagePopupDetailDto> listAllForAdmin() {
        // 활성만 보려면 findAllByPopupIsActiveTrueOrderBy... 사용
        return adminMainpagePopupRepository.findAll().stream()
                .sorted((a, b) -> {
                    int c = Integer.compare(a.getPopupSortOrder(), b.getPopupSortOrder());
                    return (c != 0) ? c : a.getCreatedAt().compareTo(b.getCreatedAt());
                })
                .map(this::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdminMainpagePopupDetailDto> listVisible(LocalDateTime now) {
        LocalDateTime ts = (now != null) ? now : LocalDateTime.now();
        return adminMainpagePopupRepository.findVisible(ts).stream()
                .map(this::toItemDto)
                .collect(Collectors.toList());
    }

    // ---------- 등록/수정/정렬/삭제 ----------

    @Override
    @Transactional
    public Long create(AdminMainpagePopupDto req) {
        validateCreate(req);

        int nextOrder = adminMainpagePopupRepository.findMaxSortOrderOfActive() + 10;

        MainpagePopupEntity entity = MainpagePopupEntity.builder()
                .popupImageUrl(req.getPopupImageUrl())
                .popupLinkUrl(req.getPopupLinkUrl())
                .popupTarget(Objects.requireNonNullElse(req.getPopupTarget(), MainpagePopupEntity.Target._self))
                .popupIsActive(req.isPopupIsActive())
                .popupSortOrder(nextOrder)
                .popupStartAt(req.getPopupStartAt())
                .popupEndAt(req.getPopupEndAt())
                .build();

        checkSchedule(entity.getPopupStartAt(), entity.getPopupEndAt());

        adminMainpagePopupRepository.save(entity);
        return entity.getPopupId();
    }

    @Override
    @Transactional
    public void update(Long popupId, AdminMainpagePopupUpdateDto req) {
        MainpagePopupEntity entity = getOrThrow(popupId);

        if (req.getPopupImageUrl() != null) entity.setPopupImageUrl(req.getPopupImageUrl());
        if (req.getPopupLinkUrl()  != null) entity.setPopupLinkUrl(req.getPopupLinkUrl());
        if (req.getPopupTarget()   != null) entity.setPopupTarget(req.getPopupTarget());
        if (req.getPopupStartAt()  != null) entity.setPopupStartAt(req.getPopupStartAt());
        if (req.getPopupEndAt()    != null) entity.setPopupEndAt(req.getPopupEndAt());
        if (req.getPopupIsActive() != null) entity.setPopupIsActive(req.getPopupIsActive());
        if (req.getPopupSortOrder()!= null) entity.setPopupSortOrder(req.getPopupSortOrder());

        checkSchedule(entity.getPopupStartAt(), entity.getPopupEndAt());
        // JPA dirty checking으로 자동 업데이트
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
        // 대체안:
        // repo.findAllById(ids).forEach(e -> e.setPopupIsActive(active));
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

    private void validateCreate(AdminMainpagePopupDto req) {
        if (req == null) throw new IllegalArgumentException("CreateReq is null");
        if (isBlank(req.getPopupImageUrl())) throw new IllegalArgumentException("popup_image_url is required");
        if (isBlank(req.getPopupLinkUrl()))  throw new IllegalArgumentException("popup_link_url is required");
        checkSchedule(req.getPopupStartAt(), req.getPopupEndAt());
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private AdminMainpagePopupDetailDto toItemDto(MainpagePopupEntity e) {
        return AdminMainpagePopupDetailDto.builder()
                .popupId(e.getPopupId())
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
}
