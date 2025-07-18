package com.boot.loiteBackend.admin.support.resource.service;

import com.boot.loiteBackend.admin.product.product.dto.AdminProductSummaryDto;
import com.boot.loiteBackend.admin.support.resource.dto.AdminSupportResourceDto;
import com.boot.loiteBackend.admin.support.resource.dto.AdminSupportResourceRequestDto;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdminSupportResourceService {
    List<AdminProductSummaryDto> getProductsList();

    AdminSupportResourceDto createResource(AdminSupportResourceRequestDto requestDto, MultipartFile file);

    AdminSupportResourceDto updateResource(Long id, AdminSupportResourceRequestDto request, MultipartFile file);

    void deleteResource(Long id);

    Page<AdminSupportResourceDto> getPagedResources(String keyword, Pageable pageable);

    AdminSupportResourceDto getResourceById(Long id);

    ResponseEntity<Resource> fileDownload(Long id);
}
