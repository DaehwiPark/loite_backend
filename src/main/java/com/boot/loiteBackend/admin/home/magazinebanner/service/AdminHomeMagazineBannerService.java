package com.boot.loiteBackend.admin.home.magazinebanner.service;

import com.boot.loiteBackend.admin.home.magazinebanner.dto.AdminHomeMagazineBannerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface AdminHomeMagazineBannerService {
    AdminHomeMagazineBannerDto.Response create(AdminHomeMagazineBannerDto.Request request, MultipartFile videoFile, MultipartFile thumbnailFile);
    AdminHomeMagazineBannerDto.Response update(Long id, AdminHomeMagazineBannerDto.Request request, MultipartFile videoFile, MultipartFile thumbnailFile);
    void delete(Long id);
    AdminHomeMagazineBannerDto.Response getOne(Long id);
    Page<AdminHomeMagazineBannerDto.Response> getList(Pageable pageable);
}