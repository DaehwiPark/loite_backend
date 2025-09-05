package com.boot.loiteBackend.web.home.fullbanner.service;

import com.boot.loiteBackend.web.home.fullbanner.dto.HomeFullBannerResponseDto;

public interface HomeFullBannerService {
    /** 대표 배너 1건 조회 (없으면 에러) */
    HomeFullBannerResponseDto getActiveDefault();
}
