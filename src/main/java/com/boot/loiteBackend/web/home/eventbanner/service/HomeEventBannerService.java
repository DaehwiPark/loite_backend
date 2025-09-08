package com.boot.loiteBackend.web.home.eventbanner.service;

import com.boot.loiteBackend.web.home.eventbanner.dto.HomeEventBannerResponseDto;

import java.util.List;

public interface HomeEventBannerService {
    List<HomeEventBannerResponseDto> getActiveBySection(int limit);
}
