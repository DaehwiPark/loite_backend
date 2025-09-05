package com.boot.loiteBackend.admin.home.hero.service;

import com.boot.loiteBackend.admin.home.hero.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface AdminHomeHeroService {

    AdminHomeHeroResponseDto create(AdminHomeHeroCreateRequestDto info,
                                    MultipartFile image,
                                    Long loginUserId);

    AdminHomeHeroResponseDto update(AdminHomeHeroUpdateRequestDto req,
                                    MultipartFile image,
                                    Long loginUserId);

    void delete(Long id, Long loginUserId);

    AdminHomeHeroResponseDto detail(Long id);

    Page<AdminHomeHeroResponseDto> list(Pageable pageable, AdminHomeHeroListRequestDto filter);
}
