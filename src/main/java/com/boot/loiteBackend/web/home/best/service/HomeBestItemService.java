package com.boot.loiteBackend.web.home.best.service;

import com.boot.loiteBackend.web.home.best.dto.HomeBestItemResponseDto;
import java.util.List;

public interface HomeBestItemService {
    List<HomeBestItemResponseDto> getBestItemList();
}
