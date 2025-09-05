package com.boot.loiteBackend.web.home.hero.service;

import com.boot.loiteBackend.domain.home.hero.mapper.HomeHeroMapper;
import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.web.home.hero.dto.HomeHeroResponseDto;
import com.boot.loiteBackend.web.home.hero.error.HomeHeroErrorCode;
import com.boot.loiteBackend.web.home.hero.repository.HomeHeroRepository;
import com.boot.loiteBackend.common.util.PageUtils; // ★ 공통 유틸 사용
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeHeroServiceImpl implements HomeHeroService {

    private final HomeHeroRepository repository;
    private final HomeHeroMapper mapper;

    /** 기본 정렬: sortOrder ASC → startAt DESC → id DESC */
    private static final Sort DEFAULT_SORT = Sort.by(
            Sort.Order.asc("sortOrder"),
            Sort.Order.desc("startAt"),
            Sort.Order.desc("id")
    );

    @Override
    public HomeHeroResponseDto getActiveOne() {
        LocalDateTime now = LocalDateTime.now();
        var list = repository.findActiveForNow(now, PageUtils.firstPage(1, DEFAULT_SORT)); // ★ 공통
        if (list.isEmpty()) {
            throw new CustomException(HomeHeroErrorCode.ACTIVE_NOT_FOUND);
        }
        return mapper.toWebResponse(list.get(0));
    }

    @Override
    public List<HomeHeroResponseDto> getActiveList(int limit) {
        if (limit < 1) {
            throw new CustomException(HomeHeroErrorCode.INVALID_LIMIT);
        }
        LocalDateTime now = LocalDateTime.now();
        var list = repository.findActiveForNow(now, PageUtils.firstPage(limit, DEFAULT_SORT)); // ★ 공통
        if (list.isEmpty()) {
            throw new CustomException(HomeHeroErrorCode.ACTIVE_NOT_FOUND);
        }
        return mapper.toWebResponseList(list);
    }
}
