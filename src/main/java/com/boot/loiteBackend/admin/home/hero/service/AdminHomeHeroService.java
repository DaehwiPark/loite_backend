package com.boot.loiteBackend.admin.home.hero.service;

import com.boot.loiteBackend.admin.home.hero.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface AdminHomeHeroService {

    AdminHomeHeroResponseDto create(AdminHomeHeroCreateRequestDto info, MultipartFile image);


    void delete(AdminHomeHeroDeleteRequestDto req);

    AdminHomeHeroResponseDto detail(Long id);

    Page<AdminHomeHeroResponseDto> list(AdminHomeHeroListRequestDto filter, int page, int size);

    AdminHomeHeroResponseDto update(AdminHomeHeroUpdateRequestDto req, MultipartFile image);
}
