package com.boot.loiteBackend.admin.home.recommend.item.service;

import com.boot.loiteBackend.admin.home.recommend.item.dto.*;
import com.boot.loiteBackend.admin.home.recommend.item.error.AdminHomeRecoItemErrorCode;
import com.boot.loiteBackend.admin.home.recommend.item.mapper.AdminHomeRecoItemMapper;
import com.boot.loiteBackend.admin.home.recommend.item.repository.AdminHomeRecoItemRepository;
import com.boot.loiteBackend.common.util.PageUtils;
import com.boot.loiteBackend.domain.home.recommend.item.entity.HomeRecoItemEntity;
import com.boot.loiteBackend.global.error.exception.CustomException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminHomeRecoItemServiceImpl implements AdminHomeRecoItemService {

    private final AdminHomeRecoItemRepository adminHomeRecoItemRepository;
    private final AdminHomeRecoItemMapper adminHomeRecoItemMapper;
    private final EntityManager em;

    private Sort defaultSort() {

        return Sort.by(Sort.Order.asc("slotNo"), Sort.Order.desc("id"));
    }

    private void assertSlotRange(Integer slotNo) {
        if (slotNo == null || slotNo < 1 || slotNo > 10) {
            throw new CustomException(AdminHomeRecoItemErrorCode.SLOT_RANGE);
        }
    }

    @Override
    @Transactional
    public AdminHomeRecoItemResponseDto create(AdminHomeRecoItemCreateDto req, Long userId) {
        if (userId == null) throw new CustomException(AdminHomeRecoItemErrorCode.UNAUTHORIZED);
        if (req == null) throw new CustomException(AdminHomeRecoItemErrorCode.INVALID_REQUEST);
        assertSlotRange(req.getSlotNo()); // 1..10

        // 섹션별 최대 10개 제한
        int count = adminHomeRecoItemRepository.countBySectionId(req.getSectionId());
        if (count >= 10) {
            throw new CustomException(AdminHomeRecoItemErrorCode.MAX_ITEMS);
        }

        final long sectionId = req.getSectionId();
        final int desired = req.getSlotNo();

        // 원하는 슬롯(desired)을 비우기: "위쪽(desired..10) 우선", 없으면 "아래쪽(1..desired-1) 끌어올리기"
        ensureDesiredSlotAvailableInSection(sectionId, desired);

        try {
            HomeRecoItemEntity e = adminHomeRecoItemMapper.toEntity(req);
            e.setSectionId(sectionId);
            e.setSlotNo(desired);
            if (e.getDisplayYn() == null || e.getDisplayYn().isBlank()) e.setDisplayYn("Y");
            e.setCreatedBy(userId);
            e.setUpdatedBy(userId);

            var saved = adminHomeRecoItemRepository.save(e);
            em.flush();
            return adminHomeRecoItemMapper.toResponseDto(saved);
        } catch (DataAccessException ex) {
            throw new CustomException(AdminHomeRecoItemErrorCode.SAVE_FAILED);
        }
    }

    /** 섹션 내에서 desired 슬롯을 반드시 비워둔다. (위쪽 우선 정책) */
    private void ensureDesiredSlotAvailableInSection(long sectionId, int desired) {
        // 1) 위쪽(desired..10)에서 첫 빈칸
        Integer freeAbove = adminHomeRecoItemRepository.findFirstFreeSlotFrom(sectionId, desired);
        if (freeAbove != null) {
            if (freeAbove > desired) {
                // [desired .. freeAbove-1] 을 +1 → desired 비움
                adminHomeRecoItemRepository.shiftRangeRightByOne(sectionId, desired, freeAbove - 1);
                em.flush();
            }
            return; // freeAbove == desired → 이미 비어 있음
        }

        // 2) 위쪽이 꽉 찼다면, 아래쪽(1..desired-1)의 마지막 빈칸을 끌어올림(왼쪽 -1)
        if (desired > 1) {
            Integer freeBelow = adminHomeRecoItemRepository.findLastFreeSlotUpTo(sectionId, desired - 1);
            if (freeBelow != null) {
                // [freeBelow+1 .. desired] 을 -1 → desired 비움
                adminHomeRecoItemRepository.shiftRangeLeftByOne(sectionId, freeBelow + 1, desired);
                em.flush();
                return;
            }
        }

        // 3) 위아래 모두 빈칸 없음 → 실패
        throw new CustomException(AdminHomeRecoItemErrorCode.SAVE_FAILED);
    }

    @Override
    @Transactional
    public AdminHomeRecoItemResponseDto update(AdminHomeRecoItemUpdateDto req, Long userId) {
        if (userId == null) throw new CustomException(AdminHomeRecoItemErrorCode.UNAUTHORIZED);
        if (req == null || req.getId() == null) throw new CustomException(AdminHomeRecoItemErrorCode.INVALID_REQUEST);

        var e = adminHomeRecoItemRepository.findById(req.getId())
                .orElseThrow(() -> new CustomException(AdminHomeRecoItemErrorCode.NOT_FOUND));

        // 1) 슬롯 이동: 동일 섹션 내에서 '다른 아이템' 보정(+1/-1) → 대상만 최종 위치로
        if (req.getSlotNo() != null) {
            int newPos = req.getSlotNo();
            assertSlotRange(newPos);
            int oldPos = e.getSlotNo();

            if (newPos != oldPos) {
                if (newPos < oldPos) {
                    // 위로 이동: [newPos .. oldPos-1] +1 (대상 제외)
                    adminHomeRecoItemRepository.shiftRangeRightByOneExcludingId(
                            e.getSectionId(), e.getId(), newPos, oldPos - 1
                    );
                    em.flush();
                } else {
                    // 아래로 이동: [oldPos+1 .. newPos] -1 (대상 제외)
                    adminHomeRecoItemRepository.shiftRangeLeftByOneExcludingId(
                            e.getSectionId(), e.getId(), oldPos + 1, newPos
                    );
                    em.flush();
                }
                // 대상 하나를 최종 위치로
                adminHomeRecoItemRepository.updateSlot(e.getId(), newPos);
                em.flush();
                e.setSlotNo(newPos);
            }
        }

        // 2) 기타 필드 패치 (slotNo/sectionId는 매퍼에서 덮어쓰지 않도록 권장)
        adminHomeRecoItemMapper.updateFromDto(req, e);

        e.setUpdatedBy(userId);
        var saved = adminHomeRecoItemRepository.save(e);
        return adminHomeRecoItemMapper.toResponseDto(saved);
    }


    @Override
    public void delete(Long id, Long userId) {
        if (userId == null) throw new CustomException(AdminHomeRecoItemErrorCode.UNAUTHORIZED);
        var e = adminHomeRecoItemRepository.findById(id)
                .orElseThrow(() -> new CustomException(AdminHomeRecoItemErrorCode.NOT_FOUND));
        adminHomeRecoItemRepository.delete(e);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminHomeRecoItemResponseDto detail(Long id) {
        var e = adminHomeRecoItemRepository.findById(id)
                .orElseThrow(() -> new CustomException(AdminHomeRecoItemErrorCode.NOT_FOUND));
        return adminHomeRecoItemMapper.toResponseDto(e);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdminHomeRecoItemResponseDto> list(Pageable pageable, String keyword) {
        Pageable safe = PageUtils.safePageable(pageable, defaultSort());
        Specification<HomeRecoItemEntity> spec = (root, q, cb) -> cb.conjunction();
        var page = adminHomeRecoItemRepository.findAll(spec, safe);
        return page.map(adminHomeRecoItemMapper::toResponseDto);
    }
}
