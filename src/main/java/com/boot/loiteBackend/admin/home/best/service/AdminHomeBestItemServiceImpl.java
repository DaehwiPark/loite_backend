package com.boot.loiteBackend.admin.home.best.service;

import com.boot.loiteBackend.admin.home.best.dto.AdminHomeBestItemCreateDto;
import com.boot.loiteBackend.admin.home.best.dto.AdminHomeBestItemResponseDto;
import com.boot.loiteBackend.admin.home.best.dto.AdminHomeBestItemUpdateDto;
import com.boot.loiteBackend.admin.home.best.error.AdminHomeBestItemErrorCode;
import com.boot.loiteBackend.admin.home.best.mapper.AdminHomeBestItemMapper;
import com.boot.loiteBackend.admin.home.best.repository.AdminHomeBestItemRepository;
import com.boot.loiteBackend.common.util.PageUtils;
import com.boot.loiteBackend.domain.home.best.entity.HomeBestItemEntity;
import com.boot.loiteBackend.global.error.exception.CustomException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminHomeBestItemServiceImpl implements AdminHomeBestItemService {

    private final AdminHomeBestItemRepository adminHomeBestItemRepository;
    private final AdminHomeBestItemMapper adminHomeBestItemMapper;
    private final EntityManager em;

    private Sort defaultSort() {
        return Sort.by(Sort.Order.asc("slotNo"), Sort.Order.desc("id"));
    }

    private void assertSlotRange(Integer slotNo) {
        if (slotNo == null || slotNo < 1 || slotNo > 10) {
            throw new CustomException(AdminHomeBestItemErrorCode.SLOT_RANGE);
        }
    }

    @Override
    @Transactional
    public AdminHomeBestItemResponseDto create(AdminHomeBestItemCreateDto req, Long userId) {
        if (userId == null)
            throw new CustomException(AdminHomeBestItemErrorCode.UNAUTHORIZED);
        if (req == null)
            throw new CustomException(AdminHomeBestItemErrorCode.INVALID_REQUEST);
        // 슬롯 번호가 1~10 범위 안에 있는지 확인
        assertSlotRange(req.getSlotNo());

        //  전체 아이템 개수 확인 (최대 10개 제한)
        int count = adminHomeBestItemRepository.countAll();
        if (count >= 10) {
            throw new CustomException(AdminHomeBestItemErrorCode.MAX_ITEMS);
        }

        // 이번에 추가하려는 슬롯 위치 값
        final int desired = req.getSlotNo();

        // ASC 정렬이라고 생각했을 때
        // (1 ~ desired-1)에 빈 슬롯이 있는지 확인 → 있으면, 그 빈 슬롯을 끌어올려서 desired 자리를 비워줌
        Integer freeBelow = (desired > 1)
                ? adminHomeBestItemRepository.findLastFreeSlotUpTo(desired - 1)
                : null;

        if (freeBelow != null) {
            // freeBelow+1 ~ desired 범위를 모두 -1 시프트
            // 기존 desired 자리에 있던 아이템은 desired-1로 이동
            //  결국 desired 자리가 비게 됨
            adminHomeBestItemRepository.shiftRangeLeftByOne(freeBelow + 1, desired);
            em.flush();
        } else {
            // 아래쪽에 빈 슬롯이 없으면 → 위쪽(desired ~ 10)에서 빈 슬롯을 찾아서 오른쪽으로 밀어냄
            Integer freeAbove = adminHomeBestItemRepository.findFirstFreeSlotFrom(desired);
            if (freeAbove == null) {
                // 위에도 아래에도 빈 슬롯이 없으면 저장 불가
                throw new CustomException(AdminHomeBestItemErrorCode.SAVE_FAILED);
            }
            if (freeAbove > desired) {
                // desired ~ (freeAbove-1) 까지를 +1 시프트
                //  desired 자리가 비게 됨
                adminHomeBestItemRepository.shiftRangeRightByOne(desired, freeAbove - 1);
                em.flush();
            }
            // (freeAbove == desired) → 애초에 원하는 자리가 비어 있었던 경우 → 시프트 불필요
        }

        try {
            HomeBestItemEntity e = adminHomeBestItemMapper.toEntity(req);
            e.setSlotNo(desired);
            if (e.getDisplayYn() == null || e.getDisplayYn().isBlank()) e.setDisplayYn("Y");
            e.setCreatedBy(userId);
            e.setUpdatedBy(userId);
            var saved = adminHomeBestItemRepository.save(e);
            em.flush();
            return adminHomeBestItemMapper.toResponseDto(saved);
        } catch (DataAccessException ex) {
            throw new CustomException(AdminHomeBestItemErrorCode.SAVE_FAILED);
        }
    }


    @Override
    @Transactional
    public AdminHomeBestItemResponseDto update(AdminHomeBestItemUpdateDto req, Long userId) {
        if (userId == null) throw new CustomException(AdminHomeBestItemErrorCode.UNAUTHORIZED);
        if (req == null || req.getId() == null) throw new CustomException(AdminHomeBestItemErrorCode.INVALID_REQUEST);

        var e = adminHomeBestItemRepository.findById(req.getId())
                .orElseThrow(() -> new CustomException(AdminHomeBestItemErrorCode.NOT_FOUND));

        // 슬롯 이동이 요청된 경우: 먼저 '다른 아이템' 순서를 보정한 후 대상의 위치를 갱신
        if (req.getSlotNo() != null) {
            int newPos = req.getSlotNo();
            assertSlotRange(newPos);

            int oldPos = e.getSlotNo();
            if (newPos != oldPos) {
                relocate(e.getId(), oldPos, newPos);
                e.setSlotNo(newPos);
            }
        }

        // 다른 필드 패치 (slotNo는 위에서 처리했고, mapper가 slotNo를 덮어쓰지 않게 설정 권장)
        adminHomeBestItemMapper.updateFromDto(req, e);

        e.setUpdatedBy(userId);
        var saved = adminHomeBestItemRepository.save(e);
        return adminHomeBestItemMapper.toResponseDto(saved);
    }


    // 기존 oldPos → newPos로 대상 1개를 재배치하면서, 구간의 나머지들을 1칸 이동해 순서를 보정한다.
    private void relocate(long id, int oldPos, int newPos) {
        if (newPos < oldPos) {
            // 위로 끌어올림: [newPos .. oldPos-1] 범위의 '다른 아이템들'을 +1 (오른쪽으로)
            adminHomeBestItemRepository.shiftRangeRightByOneExcludingId(id, newPos, oldPos - 1);
            em.flush();
        } else {
            // 아래로 내림: [oldPos+1 .. newPos] 범위의 '다른 아이템들'을 -1 (왼쪽으로)
            adminHomeBestItemRepository.shiftRangeLeftByOneExcludingId(id, oldPos + 1, newPos);
            em.flush();
        }
        // 대상 자신을 최종 위치로
        adminHomeBestItemRepository.updateSlot(id, newPos);
        em.flush();
    }


    @Override
    public void delete(Long id, Long userId) {
        if (userId == null) throw new CustomException(AdminHomeBestItemErrorCode.UNAUTHORIZED);
        var e = adminHomeBestItemRepository.findById(id)
                .orElseThrow(() -> new CustomException(AdminHomeBestItemErrorCode.NOT_FOUND));
        adminHomeBestItemRepository.delete(e);
    }


    @Override
    @Transactional(readOnly = true)
    public AdminHomeBestItemResponseDto detail(Long id) {
        var e = adminHomeBestItemRepository.findById(id)
                .orElseThrow(() -> new CustomException(AdminHomeBestItemErrorCode.NOT_FOUND));
        return adminHomeBestItemMapper.toResponseDto(e);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<AdminHomeBestItemResponseDto> list(Pageable pageable, String keyword) {
        Pageable safe = PageUtils.safePageable(pageable, defaultSort());
        Specification<HomeBestItemEntity> spec = (root, q, cb) -> cb.conjunction();
        var page = adminHomeBestItemRepository.findAll(spec, safe);
        return page.map(adminHomeBestItemMapper::toResponseDto);
    }
}
