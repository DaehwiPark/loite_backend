package com.boot.loiteBackend.web.home.hero.service;

import com.boot.loiteBackend.web.home.hero.dto.HomeHeroResponseDto;

import java.util.List;

public interface HomeHeroService {
    HomeHeroResponseDto getActiveOne();

    List<HomeHeroResponseDto> getActiveList(int limit);
}
