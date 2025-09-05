package com.boot.loiteBackend.admin.home.fullbanner.service;

import com.boot.loiteBackend.admin.home.fullbanner.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface AdminHomeFullBannerService {
    AdminHomeFullBannerResponseDto create(AdminHomeFullBannerCreateRequestDto req, MultipartFile bgImage, Long loginUserId);

    AdminHomeFullBannerResponseDto update(AdminHomeFullBannerUpdateRequestDto req, MultipartFile bgImage, Long loginUserId);

    void delete(Long id, Long loginUserId);

    AdminHomeFullBannerResponseDto detail(Long id);

    Page<AdminHomeFullBannerResponseDto> list(Pageable pageable, AdminHomeFullBannerListRequestDto filter);
}
