package com.boot.loiteBackend.web.home.eventbanner.service;

import com.boot.loiteBackend.domain.home.eventbanner.mapper.HomeEventBannerMapper;
import com.boot.loiteBackend.web.home.eventbanner.dto.HomeEventBannerResponseDto;
import com.boot.loiteBackend.web.home.eventbanner.repository.HomeEventBannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service @RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeBannerServiceImpl implements HomeEventBannerService {

    private final HomeEventBannerRepository repository;
    private final HomeEventBannerMapper mapper;

    private Sort defaultSort() {
        return Sort.by(
                Sort.Order.asc("defaultSlot"), // 대표 슬롯 우선 (NULLS LAST는 JPQL 정렬에 반영 어려우니 Repo JPQL에서 처리)
                Sort.Order.asc("sortOrder"),
                Sort.Order.desc("startAt"),
                Sort.Order.desc("id")
        );
    }

    @Override
    public List<HomeEventBannerResponseDto> getActiveBySection(String sectionCode, int limit) {
        var now = LocalDateTime.now();
        var list = repository.findActiveBySection(sectionCode, now, PageRequest.of(0, Math.max(limit,1), defaultSort()));
        return mapper.toWebResponseList(list);
    }
}
