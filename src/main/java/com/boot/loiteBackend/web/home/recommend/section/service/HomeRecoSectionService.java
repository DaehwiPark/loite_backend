package com.boot.loiteBackend.web.home.recommend.section.service;

import com.boot.loiteBackend.web.home.recommend.section.dto.HomeRecoSectionResponseDto;

import java.util.List;

public interface HomeRecoSectionService {
    List<HomeRecoSectionResponseDto> list();
}
