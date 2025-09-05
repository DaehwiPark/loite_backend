package com.boot.loiteBackend.admin.home.topbar.service;

import com.boot.loiteBackend.admin.home.topbar.dto.*;
import com.boot.loiteBackend.admin.home.topbar.repository.AdminHomeTopBarRepository;
import com.boot.loiteBackend.domain.home.topbar.entity.HomeTopBarEntity;
import com.boot.loiteBackend.domain.home.topbar.mapper.HomeTopBarMapper;
import com.boot.loiteBackend.global.error.exception.CustomException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

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

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    /** Y/N 정규화. 허용값 외 입력 시 INVALID_REQUEST */
    private static String normalizeYnOrThrow(String value, String defaultWhenBlank) {
        if (isBlank(value)) return defaultWhenBlank;
        String v = value.trim().toUpperCase();
        if (!"Y".equals(v) && !"N".equals(v)) throw new CustomException(INVALID_REQUEST);
        return v;
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
                // 전역 스코프: 모두 N으로 초기화 후 flush
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

        // 0) 요청 해석: 이번 요청이 "기본으로 설정(Y)" 의도인지 먼저 확정
        final boolean wantsDefaultY =
                dto.getDefaultYn() != null && "Y".equalsIgnoreCase(dto.getDefaultYn());

        // 1) ★중요★ 매퍼가 defaultYn을 먼저 Y로 올려버리는 상황을 차단
        //    → bulk 실행 전 flush 시점에 Y가 반영되지 않도록!
        String keepDefaultYnFromDto = dto.getDefaultYn(); // 나중에 재적용하기 위해 백업
        if (wantsDefaultY) {
            dto.setDefaultYn(null); // 매퍼가 defaultYn을 변경하지 못하게 제거
        }

        // 2) 부분 업데이트 (기타 필드만 patch)
        mapper.updateEntityFromDto(dto, entity);

        // 3) displayYn 정규화/검증
        entity.setDisplayYn(normalizeYnOrThrow(entity.getDisplayYn(), "Y"));

        try {
            if (wantsDefaultY) {
                // 4) 본인 제외 전부 N → flush (유니크 충돌 예방을 위한 실제 DB 반영)
                repository.clearDefaultAllExcept(entity.getId(), loginUserId);
                em.flush(); // 이 시점에 "기존 기본"이 이미 N으로 바뀜

                // 5) 이제 대상 엔티티만 Y로 설정
                entity.setDefaultYn("Y");
            } else if ("N".equalsIgnoreCase(keepDefaultYnFromDto)) {
                entity.setDefaultYn("N");
            } else {
                // 명시 없으면 현재값 정규화만
                entity.setDefaultYn(normalizeYnOrThrow(entity.getDefaultYn(), "N"));
            }

            // 6) 감사필드
            entity.setUpdatedBy(loginUserId);
            entity.setUpdatedAt(LocalDateTime.now(clock));

            HomeTopBarEntity saved = repository.save(entity);
            em.flush(); // 유니크 제약 즉시 검증
            return mapper.toResponse(saved);

        } catch (DataIntegrityViolationException e) {
            // 혹시 동시 요청으로 경합이 생기면 일관된 도메인 예외로 변환
            log.error(e.getMessage(), e);
            throw new CustomException(DUPLICATE_DEFAULT);
        } finally {
            // 원본 DTO 복구(테스트나 재사용 방지용; 선택 사항)
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

    @Override
    @Transactional(readOnly = true)
    public List<AdminHomeTopBarResponseDto> listAll() {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(HomeTopBarEntity::getUpdatedAt,
                        Comparator.nullsFirst(Comparator.naturalOrder())).reversed())
                .map(mapper::toResponse)
                .toList();
    }
}
