package com.boot.loiteMsBack.support.resource.service;

import com.boot.loiteMsBack.support.resource.dto.SupportResourceDto;
import com.boot.loiteMsBack.support.resource.dto.SupportResourceRequestDto;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SupportResourceService {
    SupportResourceDto createResource(SupportResourceRequestDto requestDto, MultipartFile file);
    SupportResourceDto updateResource(Long id, SupportResourceRequestDto request, MultipartFile file);
    void deleteResource(Long id);
    Page<SupportResourceDto> getPagesResources(String keyword, Pageable pageable);
    SupportResourceDto getResourceById(Long id);
    ResponseEntity<Resource> fileDownload(Long id);
}
