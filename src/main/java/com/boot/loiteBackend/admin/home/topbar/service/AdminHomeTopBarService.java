package com.boot.loiteBackend.admin.home.topbar.service;

import com.boot.loiteBackend.admin.home.topbar.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminHomeTopBarService {

    AdminHomeTopBarResponseDto create(AdminHomeTopBarCreateRequestDto dto, Long loginUser);

    AdminHomeTopBarResponseDto update(AdminHomeTopBarUpdateRequestDto dto, Long loginUserId);

    void delete(Long id, Long loginUserId);

    AdminHomeTopBarResponseDto detail(Long id);

    Page<AdminHomeTopBarResponseDto> list(Pageable pageable, String keyword);
}
