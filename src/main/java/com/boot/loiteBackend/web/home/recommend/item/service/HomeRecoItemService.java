package com.boot.loiteBackend.web.home.recommend.item.service;

import com.boot.loiteBackend.web.home.recommend.item.dto.HomeRecoItemResponseDto;

import java.util.List;

public interface HomeRecoItemService {
    List<HomeRecoItemResponseDto> findItemsBySection(Long sectionId);
}
