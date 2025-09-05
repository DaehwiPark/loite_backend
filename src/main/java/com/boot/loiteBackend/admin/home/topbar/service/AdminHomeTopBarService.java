package com.boot.loiteBackend.admin.home.topbar.service;

import com.boot.loiteBackend.admin.home.topbar.dto.*;
import java.util.List;

public interface AdminHomeTopBarService {

    AdminHomeTopBarResponseDto create(AdminHomeTopBarCreateRequestDto dto, Long loginUser);

    AdminHomeTopBarResponseDto update(AdminHomeTopBarUpdateRequestDto dto, Long loginUserId);

    void delete(Long id, Long loginUserId);

    AdminHomeTopBarResponseDto detail(Long id);

    List<AdminHomeTopBarResponseDto> listAll();
}
