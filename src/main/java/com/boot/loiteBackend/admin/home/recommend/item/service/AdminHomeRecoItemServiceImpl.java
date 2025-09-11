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
    public AdminHomeRecoItemResponseDto create(AdminHomeRecoItemCreateDto req, Long userId) {
        if (userId == null) throw new CustomException(AdminHomeRecoItemErrorCode.UNAUTHORIZED);
        if (req == null) throw new CustomException(AdminHomeRecoItemErrorCode.INVALID_REQUEST);
        assertSlotRange(req.getSlotNo());

        // 최대 10개 규칙(비즈니스) 유지
        int count = adminHomeRecoItemRepository.countBySectionId(req.getSectionId());
        Integer maxSlot = adminHomeRecoItemRepository.findMaxSlotNo(req.getSectionId());
        if (count >= 10 || (maxSlot != null && maxSlot >= 10)) {
            throw new CustomException(AdminHomeRecoItemErrorCode.MAX_ITEMS);
        }

        final long sectionId = req.getSectionId();
        final int desired = req.getSlotNo();

        // desired..10 중 첫 빈 슬롯
        Integer free = adminHomeRecoItemRepository.findFirstFreeSlotFrom(sectionId, desired);
        if (free == null) throw new CustomException(AdminHomeRecoItemErrorCode.SAVE_FAILED);

        if (free > desired) {
            adminHomeRecoItemRepository.shiftRangeRightByOne(sectionId, desired, free - 1);
            em.flush();
        }

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

    @Override
    public AdminHomeRecoItemResponseDto update(AdminHomeRecoItemUpdateDto req, Long userId) {
        if (userId == null) throw new CustomException(AdminHomeRecoItemErrorCode.UNAUTHORIZED);
        if (req == null || req.getId() == null) throw new CustomException(AdminHomeRecoItemErrorCode.INVALID_REQUEST);

        var e = adminHomeRecoItemRepository.findById(req.getId())
                .orElseThrow(() -> new CustomException(AdminHomeRecoItemErrorCode.NOT_FOUND));

        // 1) 슬롯 이동 먼저 (대상 slotNo 아직 변경하지 않음)
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
                // 2) 대상 하나만 최종 위치로
                adminHomeRecoItemRepository.updateSlot(e.getId(), newPos);
                em.flush();
                e.setSlotNo(newPos);
            }
        }

        // 다른 필드 패치 (slotNo/sectionId는 매퍼가 무시하도록 설정되어 있어야 안전ㅎ미)
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
