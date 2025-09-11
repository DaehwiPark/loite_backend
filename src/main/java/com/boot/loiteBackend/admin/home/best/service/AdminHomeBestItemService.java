package com.boot.loiteBackend.admin.home.best.service;

import com.boot.loiteBackend.admin.home.best.dto.AdminHomeBestItemCreateDto;
import com.boot.loiteBackend.admin.home.best.dto.AdminHomeBestItemResponseDto;
import com.boot.loiteBackend.admin.home.best.dto.AdminHomeBestItemUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminHomeBestItemService {
    AdminHomeBestItemResponseDto create(AdminHomeBestItemCreateDto req, Long userId);

    AdminHomeBestItemResponseDto update(AdminHomeBestItemUpdateDto req, Long userId);

    void delete(Long id, Long userId);

    AdminHomeBestItemResponseDto detail(Long id);

    Page<AdminHomeBestItemResponseDto> list(Pageable pageable, String keyword);
}
