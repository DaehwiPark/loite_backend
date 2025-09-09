package com.boot.loiteBackend.admin.home.recommend.section.service;

import com.boot.loiteBackend.admin.home.recommend.section.dto.*;
import org.springframework.data.domain.*;

public interface AdminHomeRecoSectionService {
    AdminHomeRecoSectionResponseDto create(AdminHomeRecoSectionCreateDto req, Long userId);

    AdminHomeRecoSectionResponseDto update(AdminHomeRecoSectionUpdateDto req, Long userId);

    void delete(Long id, Long userId);

    AdminHomeRecoSectionDetailResponseDto detail(Long id);

    Page<AdminHomeRecoSectionResponseDto> list(Pageable pageable, String keyword);
}
