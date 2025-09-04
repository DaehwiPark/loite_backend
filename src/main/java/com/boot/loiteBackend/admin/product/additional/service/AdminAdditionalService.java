package com.boot.loiteBackend.admin.product.additional.service;

import com.boot.loiteBackend.admin.product.additional.dto.AdminAdditionalRequestDto;
import com.boot.loiteBackend.admin.product.additional.dto.AdminAdditionalResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdminAdditionalService {

    AdminAdditionalResponseDto createAdditional(AdminAdditionalRequestDto requestDto, MultipartFile imageFile);

    AdminAdditionalResponseDto updateAdditional(Long additionalId, AdminAdditionalRequestDto requestDto, MultipartFile imageFile);

    void deleteAdditional(Long additionalId);

    AdminAdditionalResponseDto getAdditional(Long additionalId);

    Page<AdminAdditionalResponseDto> getAllAdditionals(Pageable pageable);
}

