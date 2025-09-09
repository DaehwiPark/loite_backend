package com.boot.loiteBackend.admin.home.recommend.section.service;

import com.boot.loiteBackend.admin.home.recommend.section.dto.*;
import com.boot.loiteBackend.admin.home.recommend.section.error.AdminHomeRecoSectionErrorCode;
import com.boot.loiteBackend.admin.home.recommend.section.repository.AdminHomeRecoSectionRepository;
import com.boot.loiteBackend.common.util.PageUtils;
import com.boot.loiteBackend.domain.home.recommend.section.entity.HomeRecoSectionEntity;
import com.boot.loiteBackend.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminHomeRecoSectionServiceImpl implements AdminHomeRecoSectionService {

    private final AdminHomeRecoSectionRepository adminHomeRecoSectionRepository;

    private Sort defaultSort() {
        return Sort.by(Sort.Order.asc("sortOrder"), Sort.Order.desc("id"));
    }

    @Override
    public AdminHomeRecoSectionResponseDto create(AdminHomeRecoSectionCreateDto req, Long userId) {
        if (userId == null)
            throw new CustomException(AdminHomeRecoSectionErrorCode.UNAUTHORIZED);
        if (req == null)
            throw new CustomException(AdminHomeRecoSectionErrorCode.INVALID_REQUEST);
        try {
            String linkTarget = (req.getLinkTarget() == null || req.getLinkTarget().isBlank()) ? "_self" : req.getLinkTarget();
            String displayYn = (req.getDisplayYn() == null || req.getDisplayYn().isBlank()) ? "Y" : req.getDisplayYn();

            // 정렬 위치 결정
            Integer max = adminHomeRecoSectionRepository.findMaxSortOrder();
            int nextTail = (max == null ? 0 : max + 1);
            int desired = (req.getSortOrder() == null ? nextTail : Math.max(0, req.getSortOrder()));

            // 중간 삽입이면 뒤로 밀기
            if (desired <= (max == null ? -1 : max)) {
                adminHomeRecoSectionRepository.shiftBackFrom(desired);
            }

            HomeRecoSectionEntity e = new HomeRecoSectionEntity();
            e.setTitle(req.getTitle());
            e.setLinkUrl(req.getLinkUrl());
            e.setLinkTarget(linkTarget);
            e.setDisplayYn(displayYn);
            e.setSortOrder(desired);
            e.setCreatedBy(userId);
            e.setUpdatedBy(userId);

            var saved = adminHomeRecoSectionRepository.save(e);
            return AdminHomeRecoSectionResponseDto.fromEntity(saved);
        } catch (DataAccessException ex) {
            throw new CustomException(AdminHomeRecoSectionErrorCode.SAVE_FAILED);
        }
    }

    @Override
    public AdminHomeRecoSectionResponseDto update(AdminHomeRecoSectionUpdateDto req, Long userId) {
        if (userId == null)
            throw new CustomException(AdminHomeRecoSectionErrorCode.UNAUTHORIZED);
        if (req == null || req.getId() == null)
            throw new CustomException(AdminHomeRecoSectionErrorCode.INVALID_REQUEST);

        var e = adminHomeRecoSectionRepository.findById(req.getId())
                .orElseThrow(() -> new CustomException(AdminHomeRecoSectionErrorCode.NOT_FOUND));

        if (req.getTitle() != null) e.setTitle(req.getTitle());
        if (req.getLinkUrl() != null) e.setLinkUrl(req.getLinkUrl());
        if (req.getLinkTarget() != null && !req.getLinkTarget().isBlank()) e.setLinkTarget(req.getLinkTarget());
        if (req.getDisplayYn() != null && !req.getDisplayYn().isBlank()) e.setDisplayYn(req.getDisplayYn());

        // 정렬 이동 로직
        if (req.getSortOrder() != null) {
            int oldPos = e.getSortOrder();
            int newPos = Math.max(0, req.getSortOrder());

            if (newPos != oldPos) {
                Integer max = adminHomeRecoSectionRepository.findMaxSortOrder();
                int maxPos = (max == null ? -1 : max);

                // newPos가 tail보다 크면 tail로 맞춤
                if (newPos > maxPos) newPos = maxPos;

                if (newPos < oldPos) {
                    // 위로 이동: [newPos, oldPos-1] 구간 +1
                    adminHomeRecoSectionRepository.shiftRangeUp(e.getId(), newPos, oldPos);
                } else {
                    // 아래로 이동: [oldPos+1, newPos] 구간 -1
                    adminHomeRecoSectionRepository.shiftRangeDown(e.getId(), oldPos, newPos);
                }
                e.setSortOrder(newPos);
            }
        }

        e.setUpdatedBy(userId);
        var saved = adminHomeRecoSectionRepository.save(e);
        return AdminHomeRecoSectionResponseDto.fromEntity(saved);
    }

    @Override
    public void delete(Long id, Long userId) {
        if (userId == null) throw new CustomException(AdminHomeRecoSectionErrorCode.UNAUTHORIZED);
        var e = adminHomeRecoSectionRepository.findById(id)
                .orElseThrow(() -> new CustomException(AdminHomeRecoSectionErrorCode.NOT_FOUND));
        adminHomeRecoSectionRepository.delete(e);
        // TODO 정렬값 재정렬(압축)까지 원한다면 여기서 tail 당김 쿼리 추가 해야함
    }

    @Override
    @Transactional(readOnly = true)
    public AdminHomeRecoSectionDetailResponseDto detail(Long id) {
        var e = adminHomeRecoSectionRepository.findById(id)
                .orElseThrow(() -> new CustomException(AdminHomeRecoSectionErrorCode.NOT_FOUND));
        return AdminHomeRecoSectionDetailResponseDto.fromEntity(e);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<AdminHomeRecoSectionResponseDto> list(Pageable pageable, String keyword) {
        // 안전 정렬로 덮어쓰기
        Pageable safe = PageUtils.safePageable(pageable, defaultSort());

        // 간단 검색: 타이틀 like (spec 생략)
        Specification<HomeRecoSectionEntity> spec = (root, q, cb) -> {
            if (keyword == null || keyword.isBlank()) return cb.conjunction();
            return cb.like(cb.lower(root.get("title")), "%" + keyword.toLowerCase() + "%");
        };

        var page = adminHomeRecoSectionRepository.findAll(spec, safe);
        return page.map(AdminHomeRecoSectionResponseDto::fromEntity);
    }
}
