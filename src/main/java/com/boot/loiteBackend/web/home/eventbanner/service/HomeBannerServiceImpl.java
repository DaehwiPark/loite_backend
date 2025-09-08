package com.boot.loiteBackend.web.home.eventbanner.service;

import com.boot.loiteBackend.domain.home.eventbanner.mapper.HomeEventBannerMapper;
import com.boot.loiteBackend.web.home.eventbanner.dto.HomeEventBannerResponseDto;
import com.boot.loiteBackend.web.home.eventbanner.repository.HomeEventBannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeBannerServiceImpl implements HomeEventBannerService {

    private final HomeEventBannerRepository repository;
    private final HomeEventBannerMapper mapper;

    @Override
    public List<HomeEventBannerResponseDto> getActiveBySection(int limitIgnored) {
        LocalDateTime now = LocalDateTime.now();
        var two = repository.findCurrentTwoSlots(now);
        return mapper.toWebResponseList(two);
    }
}