package com.boot.loiteBackend.admin.home.recommend.item.service;

import com.boot.loiteBackend.admin.home.recommend.item.dto.AdminHomeRecoItemCreateDto;
import com.boot.loiteBackend.admin.home.recommend.item.dto.AdminHomeRecoItemResponseDto;
import com.boot.loiteBackend.admin.home.recommend.item.dto.AdminHomeRecoItemUpdateDto;
import org.springframework.data.domain.*;

public interface AdminHomeRecoItemService {
    AdminHomeRecoItemResponseDto create(AdminHomeRecoItemCreateDto req, Long userId);

    AdminHomeRecoItemResponseDto update(AdminHomeRecoItemUpdateDto req, Long userId);

    void delete(Long id, Long userId);

    AdminHomeRecoItemResponseDto detail(Long id);

    Page<AdminHomeRecoItemResponseDto> list(Pageable pageable, String keyword);
}
