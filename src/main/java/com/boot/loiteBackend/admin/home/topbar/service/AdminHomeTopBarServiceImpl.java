package com.boot.loiteBackend.admin.home.topbar.service;

import com.boot.loiteBackend.admin.home.topbar.dto.*;
import com.boot.loiteBackend.admin.home.topbar.repository.AdminHomeTopBarRepository;
import com.boot.loiteBackend.common.util.PageUtils;
import com.boot.loiteBackend.common.util.SortUtils;
import com.boot.loiteBackend.common.util.TextUtils;
import com.boot.loiteBackend.common.util.YnUtils;
import com.boot.loiteBackend.domain.home.topbar.entity.HomeTopBarEntity;
import com.boot.loiteBackend.domain.home.topbar.mapper.HomeTopBarMapper;
import com.boot.loiteBackend.global.error.exception.CustomException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

import static com.boot.loiteBackend.admin.home.topbar.error.AdminHomeTopBarErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminHomeTopBarServiceImpl implements AdminHomeTopBarService {

    private final AdminHomeTopBarRepository repository;
    private final HomeTopBarMapper mapper;
    private final Clock clock = Clock.systemDefaultZone();

    @PersistenceContext
    private EntityManager em;

    /** 잘못된 Y/N이면 도메인 예외로 변환 */
    private static String normalizeYnOrThrow(String value, String defaultWhenBlank) {
        if (TextUtils.isBlank(value)) return defaultWhenBlank;
        String v = value.trim().toUpperCase();
        if (!YnUtils.isValidYn(v)) throw new CustomException(INVALID_REQUEST);
        return "Y".equals(v) ? "Y" : "N";
    }

    /** 허용 정렬 컬럼 */
    private static final Set<String> ALLOWED_SORTS =
            Set.of("createdAt", "updatedAt", "defaultYn", "displayYn", "homeTopBarText");

    @Override
    @Transactional(readOnly = true)
    public Page<AdminHomeTopBarResponseDto> list(Pageable pageable, String keyword) {
        // 1) 허용된 정렬만 남기고, 없다면 createdAt DESC 기본
        Sort sanitizedSort = SortUtils.whitelist(
                (pageable != null ? pageable.getSort() : null),
                ALLOWED_SORTS,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        // 2) 페이지/사이즈 안전 보정 + 정렬 적용
        Pageable sanitizedPageable = PageRequest.of(
                pageable == null ? 0 : Math.max(0, pageable.getPageNumber()),
                pageable == null ? 10 : Math.max(1, pageable.getPageSize()),
                sanitizedSort
        );
        Pageable safePageable = PageUtils.safePageable(sanitizedPageable, sanitizedSort);

        Page<HomeTopBarEntity> page = TextUtils.isBlank(keyword)
                ? repository.findAll(safePageable)
                : repository.search(keyword.trim(), safePageable);

        return page.map(mapper::toResponse);
    }

    @Override
    @Transactional
    public AdminHomeTopBarResponseDto create(AdminHomeTopBarCreateRequestDto dto, Long loginUserId) {
        if (dto == null) throw new CustomException(INVALID_REQUEST);
        if (loginUserId == null) throw new CustomException(UNAUTHORIZED);

        HomeTopBarEntity entity = mapper.toEntity(dto);

        entity.setDisplayYn(normalizeYnOrThrow(entity.getDisplayYn(), "Y"));
        entity.setDefaultYn(normalizeYnOrThrow(entity.getDefaultYn(), "N"));

        LocalDateTime now = LocalDateTime.now(clock);
        entity.setCreatedBy(loginUserId);
        entity.setUpdatedBy(loginUserId);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        try {
            if (Objects.equals(entity.getDefaultYn(), "Y")) {
                // 기존 대표 N 처리 후 flush (유니크 충돌 예방)
                repository.clearDefaultAll(loginUserId);
                em.flush();
            }
            HomeTopBarEntity saved = repository.save(entity);
            em.flush(); // unique 제약 즉시 검증
            return mapper.toResponse(saved);
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage(), e);
            throw new CustomException(DUPLICATE_DEFAULT);
        }
    }

    @Override
    @Transactional
    public AdminHomeTopBarResponseDto update(AdminHomeTopBarUpdateRequestDto dto, Long loginUserId) {
        if (dto == null) throw new CustomException(INVALID_REQUEST);
        if (loginUserId == null) throw new CustomException(UNAUTHORIZED);

        HomeTopBarEntity entity = repository.findById(dto.getId())
                .orElseThrow(() -> new CustomException(TOPBAR_NOT_FOUND));

        // "기본으로 설정(Y)" 의도 파악
        final boolean wantsDefaultY = dto.getDefaultYn() != null && "Y".equalsIgnoreCase(dto.getDefaultYn());

        // bulk 이전 flush에서 Y가 먼저 반영되지 않도록 DTO 값 잠시 제거
        String keepDefaultYnFromDto = dto.getDefaultYn();
        if (wantsDefaultY) dto.setDefaultYn(null);

        // patch
        mapper.updateEntityFromDto(dto, entity);

        // displayYn 정규화/검증
        entity.setDisplayYn(normalizeYnOrThrow(entity.getDisplayYn(), "Y"));

        try {
            if (wantsDefaultY) {
                // 본인 제외 전부 N → flush
                repository.clearDefaultAllExcept(entity.getId(), loginUserId);
                em.flush();
                // 대상만 Y
                entity.setDefaultYn("Y");
            } else if ("N".equalsIgnoreCase(keepDefaultYnFromDto)) {
                entity.setDefaultYn("N");
            } else {
                entity.setDefaultYn(normalizeYnOrThrow(entity.getDefaultYn(), "N"));
            }

            // 감사필드
            entity.setUpdatedBy(loginUserId);
            entity.setUpdatedAt(LocalDateTime.now(clock));

            HomeTopBarEntity saved = repository.save(entity);
            em.flush(); // unique 제약 즉시 검증
            return mapper.toResponse(saved);

        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage(), e);
            throw new CustomException(DUPLICATE_DEFAULT);
        } finally {
            // 원본 DTO 복구(선택)
            dto.setDefaultYn(keepDefaultYnFromDto);
        }
    }

    @Override
    @Transactional
    public void delete(Long id, Long loginUserId) {
        if (loginUserId == null) throw new CustomException(UNAUTHORIZED);
        if (!repository.existsById(id)) throw new CustomException(TOPBAR_NOT_FOUND);
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminHomeTopBarResponseDto detail(Long id) {
        HomeTopBarEntity entity = repository.findById(id)
                .orElseThrow(() -> new CustomException(TOPBAR_NOT_FOUND));
        return mapper.toResponse(entity);
    }
}
