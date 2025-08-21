package com.boot.loiteBackend.web.user.address.service;

import com.boot.loiteBackend.domain.useraddress.entity.UserAddressEntity;
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

@Service
@RequiredArgsConstructor
public class UserAddressServiceImpl implements UserAddressService {

    private final UserAddressRepository repository;
    private final UserAddressMapper mapper;

    @Override
    @Transactional
    public UserAddressDto create(Long userId, UserAddressCreateDto req) {
        if (req.isDefault()) {
            repository.resetDefaultForUser(userId);
        }

        // MapStruct로 변환
        UserAddressEntity entity = mapper.toEntity(req);
        // 서비스 레이어에서 보완 세팅
        entity.setUserId(userId);
        entity.setDeleted(false);

        // 저장 후 DTO 변환
        return mapper.toDto(repository.save(entity));
    }

    @Override
    @Transactional
    public UserAddressDto update(Long userId, Long addressId, UserAddressUpdateDto req) {
        UserAddressEntity e = repository.findByIdAndUserIdAndIsDeletedFalse(addressId, userId)
                .orElseThrow(() -> new CustomException(UserAddressErrorCode.ADDRESS_NOT_FOUND));

        // 부분 업데이트 (null 무시)
        mapper.updateEntityFromDto(req, e);

        // 기본 배송지 지정/해제는 비즈니스 규칙으로 처리
        if (req.getIsDefault() != null) {
            if (req.getIsDefault()) {
                repository.resetDefaultForUser(userId);
                e.setDefault(true);
            } else {
                e.setDefault(false);
            }
        }

        return mapper.toDto(e);
    }

    @Override
    @Transactional
    public void delete(Long userId, Long addressId) {
        int updated = repository.softDelete(addressId, userId);
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

        return repository.search(userId, keyword, sorted).map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public UserAddressDto detail(Long userId, Long addressId) {
        UserAddressEntity e = repository.findByIdAndUserIdAndIsDeletedFalse(addressId, userId)
                .orElseThrow(() -> new CustomException(UserAddressErrorCode.ADDRESS_NOT_FOUND));
        return mapper.toDto(e);
    }

    @Override
    @Transactional(readOnly = true)
    public UserAddressDto getDefault(Long userId) {
        return repository.findByUserIdAndIsDefaultTrueAndIsDeletedFalse(userId)
                .map(mapper::toDto)
                .orElseThrow(() -> new CustomException(UserAddressErrorCode.DEFAULT_ADDRESS_NOT_FOUND));
    }

    @Override
    @Transactional
    public void setDefault(Long userId, Long addressId) {
        // 1) 기존 기본 플래그 해제 (flush + clear)
        repository.resetDefaultForUser(userId);

        // 2) 대상 재조회 (영속)
        UserAddressEntity target = repository.findByIdAndUserIdAndIsDeletedFalse(addressId, userId)
                .orElseThrow(() -> new CustomException(UserAddressErrorCode.ADDRESS_NOT_FOUND));

        // 3) 지정
        if (!target.isDefault()) {
            target.setDefault(true); // 영속 상태라 커밋 시 반영
        }
    }

}
