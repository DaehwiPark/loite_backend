package com.boot.loiteBackend.web.user.address.service;

import com.boot.loiteBackend.domain.user.address.entity.UserAddressEntity;
import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.web.user.address.dto.UserAddressCreateDto;
import com.boot.loiteBackend.web.user.address.dto.UserAddressDto;
import com.boot.loiteBackend.web.user.address.dto.UserAddressUpdateDto;
import com.boot.loiteBackend.web.user.address.error.UserAddressErrorCode;
import com.boot.loiteBackend.web.user.address.mapper.UserAddressMapper;
import com.boot.loiteBackend.web.user.address.repository.UserAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Locale;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserAddressServiceImpl implements UserAddressService {

    private final UserAddressRepository repository;
    private final UserAddressMapper mapper;

    /* ===== 유틸 ===== */
    private static String normalizeYn(String v) {
        if (!StringUtils.hasText(v)) return null;
        v = v.trim().toUpperCase(Locale.ROOT);
        if (Objects.equals(v, "Y") || Objects.equals(v, "N")) return v;
        // 잘못 들어오면 방어적으로 null 반환(업데이트 시 무시)
        return null;
    }

    private static boolean isY(String v) {
        return "Y".equalsIgnoreCase(v);
    }

    @Override
    @Transactional
    public UserAddressDto create(Long userId, UserAddressCreateDto req) {
        // req.getDefaultYn() 이 "Y"면 기존 기본 배송지 해제
        String wantDefault = normalizeYn(req.getDefaultYn());
        if (isY(wantDefault)) {
            repository.resetDefaultForUser(userId); // 모든 주소 DEFAULT_YN = 'N'
        }

        // MapStruct로 변환
        UserAddressEntity entity = mapper.toEntity(req);
        entity.setUserId(userId);
        // 삭제는 신규 항상 'N'
        entity.setDeleteYn("N");
        // 기본 여부는 요청 값 우선, 없으면 N
        entity.setDefaultYn(isY(wantDefault) ? "Y" : "N");

        return mapper.toDto(repository.save(entity));
    }

    @Override
    @Transactional
    public UserAddressDto update(Long userId, Long addressId, UserAddressUpdateDto req) {
        // 삭제되지 않은 내 주소만
        UserAddressEntity e = repository.findByIdAndUserIdAndDeleteYn(addressId, userId, "N")
                .orElseThrow(() -> new CustomException(UserAddressErrorCode.ADDRESS_NOT_FOUND));

        // 부분 업데이트 (문자열 등은 매퍼에서 처리) - defaultYn은 매퍼에서 제외 권장
        mapper.updateEntityFromDto(req, e);

        // 기본 배송지 플래그 비즈니스 규칙
        String defaultYn = normalizeYn(req.getDefaultYn());
        if (defaultYn != null) {
            if (isY(defaultYn)) {
                // 현재 주소를 제외하고 모두 N으로
                repository.resetDefaultForUserExcept(userId, addressId);
                e.setDefaultYn("Y"); // 영속 상태이므로 더티체킹으로 반영됨
            } else {
                e.setDefaultYn("N");
            }
        }

        // 필요시 명시적 save를 호출해도 무방하지만, 더티체킹으로 충분
        return mapper.toDto(e);
    }

    @Override
    @Transactional
    public void delete(Long userId, Long addressId) {
        // 소프트 삭제: DELETE_YN = 'Y'
        int updated = repository.softDelete(addressId, userId); // 내부에서 update ... set DELETE_YN='Y'
        if (updated == 0) {
            throw new CustomException(UserAddressErrorCode.ADDRESS_NOT_FOUND);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserAddressDto> list(Long userId, String q, Pageable pageable) {
        String keyword = StringUtils.hasText(q) ? q.trim() : null;

        Pageable sorted = pageable;
        if (pageable.getSort().isUnsorted()) {
            sorted = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "createdAt")
            );
        }

        // 삭제되지 않은 목록만
        return repository.search(userId, keyword, sorted).map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public UserAddressDto detail(Long userId, Long addressId) {
        UserAddressEntity e = repository.findByIdAndUserIdAndDeleteYn(addressId, userId, "N")
                .orElseThrow(() -> new CustomException(UserAddressErrorCode.ADDRESS_NOT_FOUND));
        return mapper.toDto(e);
    }

    @Override
    @Transactional(readOnly = true)
    public UserAddressDto getDefault(Long userId) {
        return repository.findByUserIdAndDefaultYnAndDeleteYn(userId, "Y", "N")
                .map(mapper::toDto)
                .orElseThrow(() -> new CustomException(UserAddressErrorCode.DEFAULT_ADDRESS_NOT_FOUND));
    }

    @Override
    @Transactional
    public void setDefault(Long userId, Long addressId) {
        // 1) 기존 기본 해제
        repository.resetDefaultForUser(userId);

        // 2) 대상 검증 후 기본 설정
        UserAddressEntity target = repository.findByIdAndUserIdAndDeleteYn(addressId, userId, "N")
                .orElseThrow(() -> new CustomException(UserAddressErrorCode.ADDRESS_NOT_FOUND));

        if (!isY(target.getDefaultYn())) {
            target.setDefaultYn("Y");
        }
    }

    @Override
    @Transactional
    public void unDefault(Long userId) {
        UserAddressEntity currentDefault = repository.findByUserIdAndDefaultYnAndDeleteYn(userId, "Y", "N")
                .orElseThrow(() -> new CustomException(UserAddressErrorCode.DEFAULT_ADDRESS_NOT_FOUND));
        currentDefault.setDefaultYn("N");
    }
}