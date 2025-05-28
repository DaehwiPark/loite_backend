package com.boot.loiteMsBack.support.resource.service;

import com.boot.loiteMsBack.support.resource.dto.SupportResourceDto;
import com.boot.loiteMsBack.support.resource.dto.SupportResourceRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SupportResourceService {

    SupportResourceDto createResource(SupportResourceRequestDto requestDto, MultipartFile file);

    SupportResourceDto updateResource(Long id, SupportResourceRequestDto request, MultipartFile file);

    void deleteResource(Long id);

    List<SupportResourceDto> getAllResources();

    SupportResourceDto getResourceById(Long id);
}
