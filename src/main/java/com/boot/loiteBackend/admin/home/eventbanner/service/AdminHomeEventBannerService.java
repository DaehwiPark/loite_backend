package com.boot.loiteBackend.admin.home.eventbanner.service;

import com.boot.loiteBackend.admin.home.eventbanner.dto.AdminHomeEventBannerCreateRequestDto;
import com.boot.loiteBackend.admin.home.eventbanner.dto.AdminHomeEventBannerListRequestDto;
import com.boot.loiteBackend.admin.home.eventbanner.dto.AdminHomeEventBannerResponseDto;
import com.boot.loiteBackend.admin.home.eventbanner.dto.AdminHomeEventBannerUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface AdminHomeEventBannerService {
    AdminHomeEventBannerResponseDto create(AdminHomeEventBannerCreateRequestDto request,
                                           MultipartFile pcImage, MultipartFile mobileImage,
                                           Long loginUserId);

    AdminHomeEventBannerResponseDto update(AdminHomeEventBannerUpdateRequestDto request,
                                           MultipartFile pcImage, MultipartFile mobileImage,
                                           Long loginUserId);

    void delete(Long id, Long loginUserId);

    AdminHomeEventBannerResponseDto detail(Long id);

    Page<AdminHomeEventBannerResponseDto> list(Pageable pageable, AdminHomeEventBannerListRequestDto filter);
}
