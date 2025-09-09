package com.boot.loiteBackend.web.home.eventbanner.service;

import com.boot.loiteBackend.domain.home.eventbanner.mapper.HomeEventBannerMapper;
import com.boot.loiteBackend.web.home.eventbanner.dto.HomeEventBannerResponseDto;
import com.boot.loiteBackend.web.home.eventbanner.repository.HomeEventBannerRepository;
import com.boot.loiteBackend.web.home.eventbanner.error.HomeEventBannerErrorCode;
import com.boot.loiteBackend.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeEventBannerServiceImpl implements HomeEventBannerService {

    private final HomeEventBannerRepository repository;
    private final HomeEventBannerMapper mapper;

    @Override
    public List<HomeEventBannerResponseDto> getActiveBySection(int limit) {
        if (limit <= 0) throw new CustomException(HomeEventBannerErrorCode.INVALID_REQUEST);

        var now = LocalDateTime.now();
        var list = repository.findCurrentTwoSlots(now);

        // 최대 limit 개수만 잘라서 반환
        return mapper.toWebResponseList(list.stream().limit(limit).toList());
    }
}
